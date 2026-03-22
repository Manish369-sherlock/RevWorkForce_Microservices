package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.ExpenseActionRequest;
import com.revworkforce.employeemanagementservice.dto.ExpenseRequest;
import com.revworkforce.employeemanagementservice.exception.BadRequestException;
import com.revworkforce.employeemanagementservice.exception.ResourceNotFoundException;
import com.revworkforce.employeemanagementservice.model.Employee;
import com.revworkforce.employeemanagementservice.model.Expense;
import com.revworkforce.employeemanagementservice.model.ExpenseItem;
import com.revworkforce.employeemanagementservice.model.enums.ExpenseCategory;
import com.revworkforce.employeemanagementservice.model.enums.ExpenseStatus;
import com.revworkforce.employeemanagementservice.model.enums.NotificationType;
import com.revworkforce.employeemanagementservice.model.enums.Role;
import com.revworkforce.employeemanagementservice.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class ExpenseService {
    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private EmployeeService employeeService;
    @Autowired private NotificationService notificationService;
    @Value("${app.expense.receipt-dir:uploads/expense-receipts}")
    private String receiptDir;

    @Transactional
    public Expense createExpense(String email, ExpenseRequest request) {
        Employee employee = employeeService.getEmployeeByEmail(email);

        Expense expense = Expense.builder()
                .employee(employee)
                .title(request.getTitle())
                .description(request.getDescription())
                .category(parseCategory(request.getCategory()))
                .totalAmount(request.getTotalAmount())
                .currency(request.getCurrency() != null ? request.getCurrency() : "INR")
                .expenseDate(request.getExpenseDate())
                .vendorName(request.getVendorName())
                .invoiceNumber(request.getInvoiceNumber())
                .receiptFileName(request.getReceiptFileName())
                .status(ExpenseStatus.DRAFT)
                .build();

        if (request.getItems() != null) {
            for (ExpenseRequest.ExpenseItemRequest itemReq : request.getItems()) {
                ExpenseItem item = ExpenseItem.builder()
                        .description(itemReq.getDescription())
                        .amount(itemReq.getAmount())
                        .quantity(itemReq.getQuantity() != null ? itemReq.getQuantity() : 1)
                        .build();
                expense.addItem(item);
            }
        }

        Expense saved = expenseRepository.save(expense);
        saveReceiptFile(saved, request.getReceiptBase64(), request.getReceiptFileName());
        return expenseRepository.save(saved);
    }

    @Transactional
    public Expense submitExpense(String email, Integer expenseId) {
        Employee employee = employeeService.getEmployeeByEmail(email);
        Expense expense = getExpenseById(expenseId);

        validateOwnership(expense, employee);
        if (expense.getStatus() != ExpenseStatus.DRAFT) {
            throw new BadRequestException("Only draft expenses can be submitted.");
        }

        expense.setStatus(ExpenseStatus.SUBMITTED);
        expense.setSubmittedDate(LocalDateTime.now());
        Expense saved = expenseRepository.save(expense);

        if (employee.getManager() != null) {
            notificationService.sendNotification(
                    employee.getManager(), "Expense Submitted",
                    "New expense claim from " + employee.getFirstName() + " " + employee.getLastName()
                            + " — ₹" + expense.getTotalAmount(),
                    NotificationType.EXPENSE_SUBMITTED, saved.getExpenseId(), "EXPENSE");
        }

        return saved;
    }

    public Page<Expense> getMyExpenses(String email, Pageable pageable) {
        Employee employee = employeeService.getEmployeeByEmail(email);
        return expenseRepository.findByEmployeeEmployeeId(employee.getEmployeeId(), pageable);
    }

    public Page<Expense> getTeamExpenses(String email, Pageable pageable) {
        Employee manager = employeeService.getEmployeeByEmail(email);
        return expenseRepository.findTeamExpensesByStatus(
                manager.getEmployeeId(), ExpenseStatus.SUBMITTED, pageable);
    }

    @Transactional
    public Expense managerAction(String email, Integer expenseId, ExpenseActionRequest request) {
        Employee manager = employeeService.getEmployeeByEmail(email);
        Expense expense = getExpenseById(expenseId);

        if (expense.getStatus() != ExpenseStatus.SUBMITTED) {
            throw new BadRequestException("This expense is not pending manager approval.");
        }

        Employee expenseOwner = expense.getEmployee();
        if (expenseOwner.getManager() == null ||
                !expenseOwner.getManager().getEmployeeId().equals(manager.getEmployeeId())) {
            if (manager.getRole() != Role.ADMIN) {
                throw new BadRequestException("You are not authorized to action this expense.");
            }
        }

        if ("APPROVED".equalsIgnoreCase(request.getAction())) {
            expense.setStatus(ExpenseStatus.MANAGER_APPROVED);
            expense.setManagerComments(request.getComments());
            expense.setActionedBy(manager);
            expense.setManagerActionDate(LocalDateTime.now());

            notificationService.sendNotification(expenseOwner, "Expense Approved",
                    "Your expense '" + expense.getTitle() + "' was approved by manager. Pending finance review.",
                    NotificationType.EXPENSE_APPROVED, expenseId, "EXPENSE");
        } else if ("REJECTED".equalsIgnoreCase(request.getAction())) {
            expense.setStatus(ExpenseStatus.REJECTED);
            expense.setRejectionReason(request.getComments());
            expense.setActionedBy(manager);
            expense.setManagerActionDate(LocalDateTime.now());

            notificationService.sendNotification(expenseOwner, "Expense Rejected",
                    "Your expense '" + expense.getTitle() + "' was rejected by manager: " + request.getComments(),
                    NotificationType.EXPENSE_REJECTED, expenseId, "EXPENSE");
        } else {
            throw new BadRequestException("Invalid action. Use APPROVED or REJECTED.");
        }

        return expenseRepository.save(expense);
    }

    public Page<Expense> getFinancePendingExpenses(Pageable pageable) {
        return expenseRepository.findByStatus(ExpenseStatus.MANAGER_APPROVED, pageable);
    }

    public Page<Expense> getAllExpenses(ExpenseStatus status, Pageable pageable) {
        if (status != null) {
            return expenseRepository.findByStatus(status, pageable);
        }
        return expenseRepository.findAll(pageable);
    }

    @Transactional
    public Expense financeAction(String email, Integer expenseId, ExpenseActionRequest request) {
        Employee financeUser = employeeService.getEmployeeByEmail(email);
        Expense expense = getExpenseById(expenseId);

        if (expense.getStatus() != ExpenseStatus.MANAGER_APPROVED
                && expense.getStatus() != ExpenseStatus.FINANCE_APPROVED) {
            throw new BadRequestException("This expense is not pending finance action.");
        }

        Employee expenseOwner = expense.getEmployee();

        switch (request.getAction().toUpperCase()) {
            case "APPROVED" -> {
                expense.setStatus(ExpenseStatus.FINANCE_APPROVED);
                expense.setFinanceComments(request.getComments());
                expense.setFinanceActionedBy(financeUser);
                expense.setFinanceActionDate(LocalDateTime.now());
                notificationService.sendNotification(expenseOwner, "Expense Finance Approved",
                        "Your expense '" + expense.getTitle() + "' has been approved by finance.",
                        NotificationType.EXPENSE_APPROVED, expenseId, "EXPENSE");
            }
            case "REJECTED" -> {
                expense.setStatus(ExpenseStatus.REJECTED);
                expense.setRejectionReason(request.getComments());
                expense.setFinanceActionedBy(financeUser);
                expense.setFinanceActionDate(LocalDateTime.now());
                notificationService.sendNotification(expenseOwner, "Expense Rejected",
                        "Your expense '" + expense.getTitle() + "' was rejected by finance: " + request.getComments(),
                        NotificationType.EXPENSE_REJECTED, expenseId, "EXPENSE");
            }
            case "REIMBURSED" -> {
                expense.setStatus(ExpenseStatus.REIMBURSED);
                expense.setFinanceComments(request.getComments());
                expense.setFinanceActionedBy(financeUser);
                expense.setReimbursedDate(LocalDateTime.now());
                notificationService.sendNotification(expenseOwner, "Expense Reimbursed",
                        "Your expense '" + expense.getTitle() + "' of ₹" + expense.getTotalAmount() + " has been reimbursed!",
                        NotificationType.EXPENSE_REIMBURSED, expenseId, "EXPENSE");
            }
            default -> throw new BadRequestException("Invalid action. Use APPROVED, REJECTED, or REIMBURSED.");
        }

        return expenseRepository.save(expense);
    }

    public Expense getExpenseById(Integer id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public ReceiptFileData getExpenseReceipt(String email, Integer expenseId) {
        Employee requester = employeeService.getEmployeeByEmail(email);
        Expense expense = getExpenseById(expenseId);

        ensureReceiptAccess(requester, expense);

        if (expense.getReceiptUrl() == null || expense.getReceiptUrl().isBlank()) {
            throw new ResourceNotFoundException("No uploaded receipt found for this expense.");
        }

        try {
            Path receiptPath = Paths.get(expense.getReceiptUrl()).normalize();
            if (!Files.exists(receiptPath)) {
                throw new ResourceNotFoundException("Uploaded receipt file not found.");
            }

            Resource resource = new UrlResource(receiptPath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("Uploaded receipt file is not accessible.");
            }

            String contentType = Files.probeContentType(receiptPath);
            if (contentType == null || contentType.isBlank()) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            String fileName = expense.getReceiptFileName() != null && !expense.getReceiptFileName().isBlank()
                    ? expense.getReceiptFileName()
                    : receiptPath.getFileName().toString();

            return new ReceiptFileData(resource, fileName, contentType);
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("Uploaded receipt is unavailable.");
        } catch (IOException e) {
            throw new BadRequestException("Unable to read uploaded receipt file.");
        }
    }

    private void validateOwnership(Expense expense, Employee employee) {
        if (!expense.getEmployee().getEmployeeId().equals(employee.getEmployeeId())) {
            throw new BadRequestException("You can only modify your own expenses.");
        }
    }

    private ExpenseCategory parseCategory(String category) {
        if (category == null) return ExpenseCategory.OTHER;
        try {
            return ExpenseCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ExpenseCategory.OTHER;
        }
    }

    private void saveReceiptFile(Expense expense, String receiptBase64, String originalFileName) {
        if (receiptBase64 == null || receiptBase64.isBlank()) {
            return;
        }

        try {
            String[] parts = receiptBase64.split(",", 2);
            String meta = parts.length > 1 ? parts[0] : "";
            String payload = parts.length > 1 ? parts[1] : parts[0];

            byte[] data = Base64.getDecoder().decode(payload);

            String extension = ".bin";
            if (meta.contains("image/png")) extension = ".png";
            else if (meta.contains("image/jpeg") || meta.contains("image/jpg")) extension = ".jpg";
            else if (meta.contains("application/pdf")) extension = ".pdf";
            else if (meta.contains("image/webp")) extension = ".webp";

            Path directory = Paths.get(receiptDir).toAbsolutePath().normalize();
            Files.createDirectories(directory);

            String fileName = "expense-" + expense.getExpenseId() + "-" + System.currentTimeMillis() + extension;
            Path target = directory.resolve(fileName).normalize();
            Files.write(target, data);

            expense.setReceiptUrl(target.toString());
            if (originalFileName != null && !originalFileName.isBlank()) {
                expense.setReceiptFileName(originalFileName);
            } else {
                expense.setReceiptFileName(fileName);
            }
        } catch (Exception e) {
            throw new BadRequestException("Unable to save uploaded receipt file.");
        }
    }

    private void ensureReceiptAccess(Employee requester, Expense expense) {
        Employee owner = expense.getEmployee();
        if (owner.getEmployeeId().equals(requester.getEmployeeId())) {
            return;
        }

        if (requester.getRole() == Role.ADMIN) {
            return;
        }

        if (requester.getRole() == Role.MANAGER
                && owner.getManager() != null
                && owner.getManager().getEmployeeId().equals(requester.getEmployeeId())) {
            return;
        }

        throw new BadRequestException("You are not authorized to view this receipt.");
    }

    public record ReceiptFileData(Resource resource, String fileName, String contentType) {}
}

