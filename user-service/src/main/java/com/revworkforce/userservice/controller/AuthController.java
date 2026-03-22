package com.revworkforce.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.revworkforce.userservice.config.JwtUtil;
import com.revworkforce.userservice.dto.ApiResponse;
import com.revworkforce.userservice.dto.LoginRequest;
import com.revworkforce.userservice.dto.RefreshTokenRequest;
import com.revworkforce.userservice.dto.ResendOtpRequest;
import com.revworkforce.userservice.dto.VerifyOtpRequest;
import com.revworkforce.userservice.exception.IpBlockedException;
import com.revworkforce.userservice.exception.ResourceNotFoundException;
import com.revworkforce.userservice.model.ActivityLog;
import com.revworkforce.userservice.model.Employee;
import com.revworkforce.userservice.model.RefreshToken;
import com.revworkforce.userservice.model.enums.Role;
import com.revworkforce.userservice.repository.ActivityLogRepository;
import com.revworkforce.userservice.repository.EmployeeRepository;
import com.revworkforce.userservice.service.IpAccessControlService;
import com.revworkforce.userservice.service.OtpService;
import com.revworkforce.userservice.service.RefreshTokenService;
import com.revworkforce.userservice.util.NetworkIpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ActivityLogRepository activityLogRepository;
    @Autowired
    private IpAccessControlService ipAccessControlService;
    @Autowired
    private OtpService otpService;

    @Value("${otp.fail-open-on-email-error:false}")
    private boolean otpFailOpenOnEmailError;
    @Value("${otp.skip-for-local:false}")
    private boolean otpSkipForLocal;
    @Value("${otp.enforce-for-all:true}")
    private boolean otpEnforceForAll;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String ipAddress = NetworkIpUtil.resolveClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + request.getEmail()));

        if (employee.getRole() != Role.ADMIN && !ipAccessControlService.isIpAllowed(ipAddress)) {
            activityLogRepository.save(ActivityLog.builder()
                    .performedBy(employee)
                    .action("LOGIN_BLOCKED_IP")
                    .entityType("AUTH")
                    .entityId(employee.getEmployeeId())
                    .details("Login blocked — IP not whitelisted: " + ipAddress)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .status("BLOCKED")
                    .build());

            throw new IpBlockedException(
                    "Access denied. Your IP address (" + ipAddress + ") is not whitelisted. Please contact your administrator.");
        }

        boolean requiresTwoFactor = employee.getRole() != Role.ADMIN
                && (otpEnforceForAll || Boolean.TRUE.equals(employee.getTwoFactorEnabled()));
        if (requiresTwoFactor) {
            if (otpSkipForLocal && !otpEnforceForAll) {
                log.warn("2FA bypassed for {} because otp.skip-for-local is enabled", employee.getEmail());
                activityLogRepository.save(ActivityLog.builder()
                        .performedBy(employee)
                        .action("2FA_BYPASSED_LOCAL")
                        .entityType("AUTH")
                        .entityId(employee.getEmployeeId())
                        .details("2FA bypassed due to local/dev configuration")
                        .ipAddress(ipAddress)
                        .userAgent(userAgent)
                        .status("WARNING")
                        .build());
                return buildLoginResponse(employee, userDetails, ipAddress, userAgent);
            }
            try {
                String preAuthToken = otpService.generateAndSendOtp(employee);

                activityLogRepository.save(ActivityLog.builder()
                        .performedBy(employee)
                        .action("2FA_OTP_SENT")
                        .entityType("AUTH")
                        .entityId(employee.getEmployeeId())
                        .details("2FA OTP sent to email for login verification")
                        .ipAddress(ipAddress)
                        .userAgent(userAgent)
                        .status("PENDING")
                        .build());

                Map<String, Object> twoFactorData = new HashMap<>();
                twoFactorData.put("twoFactorRequired", true);
                twoFactorData.put("preAuthToken", preAuthToken);
                twoFactorData.put("maskedEmail", maskEmail(employee.getEmail()));

                return ResponseEntity.ok(new ApiResponse(true, "Verification code sent to your email", twoFactorData));
            } catch (RuntimeException ex) {
                if (!otpFailOpenOnEmailError || otpEnforceForAll) {
                    throw ex;
                }

                log.warn("OTP email failed for {}. Allowing fail-open login due to config. Reason: {}", employee.getEmail(), ex.getMessage());
                activityLogRepository.save(ActivityLog.builder()
                        .performedBy(employee)
                        .action("2FA_BYPASSED_EMAIL_FAILURE")
                        .entityType("AUTH")
                        .entityId(employee.getEmployeeId())
                        .details("2FA temporarily bypassed because OTP email failed to send")
                        .ipAddress(ipAddress)
                        .userAgent(userAgent)
                        .status("WARNING")
                        .build());
                return buildLoginResponse(employee, userDetails, ipAddress, userAgent);
            }
        }

        return buildLoginResponse(employee, userDetails, ipAddress, userAgent);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request, HttpServletRequest httpRequest) {
        String ipAddress = NetworkIpUtil.resolveClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");

        Employee employee = otpService.verifyOtp(request.getPreAuthToken(), request.getOtp());
        UserDetails userDetails = userDetailsService.loadUserByUsername(employee.getEmail());

        activityLogRepository.save(ActivityLog.builder()
                .performedBy(employee)
                .action("2FA_VERIFIED")
                .entityType("AUTH")
                .entityId(employee.getEmployeeId())
                .details("2FA OTP verified successfully")
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .status("SUCCESS")
                .build());

        return buildLoginResponse(employee, userDetails, ipAddress, userAgent);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        String newPreAuthToken = otpService.resendOtp(request.getPreAuthToken());

        Map<String, Object> data = new HashMap<>();
        data.put("preAuthToken", newPreAuthToken);

        return ResponseEntity.ok(new ApiResponse(true, "New verification code sent to your email", data));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        Employee employee = refreshToken.getEmployee();

        UserDetails userDetails = userDetailsService.loadUserByUsername(employee.getEmail());

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", employee.getRole().name());
        extraClaims.put("employeeId", employee.getEmployeeId());
        extraClaims.put("name", employee.getFirstName() + " " + employee.getLastName());
        String newAccessToken = jwtUtil.generateToken(extraClaims, userDetails);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("accessToken", newAccessToken);
        responseData.put("refreshToken", refreshToken.getToken());
        responseData.put("tokenType", "Bearer");

        return ResponseEntity.ok(new ApiResponse(true, "Token refreshed successfully", responseData));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest httpRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String email = auth.getName();
            refreshTokenService.revokeTokenByEmployee(email);

            Employee employee = employeeRepository.findByEmail(email).orElse(null);
            if (employee != null) {
                activityLogRepository.save(ActivityLog.builder()
                        .performedBy(employee)
                        .action("LOGOUT")
                        .entityType("AUTH")
                        .entityId(employee.getEmployeeId())
                        .details("Employee logged out")
                        .ipAddress(NetworkIpUtil.resolveClientIp(httpRequest))
                        .status("SUCCESS")
                        .build());
            }
        }
        return ResponseEntity.ok(new ApiResponse(true, "Logged out successfully. All refresh tokens revoked."));
    }

    private ResponseEntity<ApiResponse> buildLoginResponse(Employee employee, UserDetails userDetails, String ipAddress, String userAgent) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", employee.getRole().name());
        extraClaims.put("employeeId", employee.getEmployeeId());
        extraClaims.put("name", employee.getFirstName() + " " + employee.getLastName());
        String accessToken = jwtUtil.generateToken(extraClaims, userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(employee.getEmail());

        activityLogRepository.save(ActivityLog.builder()
                .performedBy(employee)
                .action("LOGIN_SUCCESS")
                .entityType("AUTH")
                .entityId(employee.getEmployeeId())
                .details("Successful login from IP: " + ipAddress)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .status("SUCCESS")
                .build());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("accessToken", accessToken);
        responseData.put("refreshToken", refreshToken.getToken());
        responseData.put("tokenType", "Bearer");
        responseData.put("employeeId", employee.getEmployeeId());
        responseData.put("employeeCode", employee.getEmployeeCode());
        responseData.put("name", employee.getFirstName() + " " + employee.getLastName());
        responseData.put("email", employee.getEmail());
        responseData.put("role", employee.getRole().name());

        return ResponseEntity.ok(new ApiResponse(true, "Login Successful", responseData));
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 2) {
            return email.charAt(0) + "***" + email.substring(atIndex);
        }
        return email.charAt(0) + "***" + email.charAt(atIndex - 1) + email.substring(atIndex);
    }
}

