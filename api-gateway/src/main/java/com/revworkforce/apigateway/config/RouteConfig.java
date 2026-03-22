package com.revworkforce.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class RouteConfig {
    @Bean
    public RouterFunction<ServerResponse> leaveServiceRoute() {
        return route("leave-service")
                .route(path("/api/employees/leaves/**")
                        .or(path("/api/employees/leave-balance/**"))
                        .or(path("/api/employees/holidays/**"))
                        .or(path("/api/employees/attendance/**"))
                        .or(path("/api/manager/leaves/**"))
                        .or(path("/api/manager/leave-calendar/**"))
                        .or(path("/api/manager/leave-analysis/**"))
                        .or(path("/api/manager/attendance/**"))
                        .or(path("/api/admin/leaves/**"))
                        .or(path("/api/admin/leave-types/**"))
                        .or(path("/api/admin/holidays/**"))
                        .or(path("/api/admin/leave-balances/**"))
                        .or(path("/api/admin/attendance/**"))
                        .or(path("/api/admin/office-locations/**")), http())
                .filter(lb("LEAVE-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> performanceServiceRoute() {
        return route("performance-service")
                .route(path("/api/employees/reviews/**")
                        .or(path("/api/employees/reviews"))
                        .or(path("/api/employees/goals/**"))
                        .or(path("/api/employees/goals"))
                        .or(path("/api/manager/reviews/**"))
                        .or(path("/api/manager/reviews"))
                        .or(path("/api/manager/goals/**"))
                        .or(path("/api/manager/goals"))
                        .or(path("/api/manager/performance/**"))
                        .or(path("/api/admin/performance/**")), http())
                .filter(lb("PERFORMANCE-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> employeeMgmtServiceRoute() {
        return route("employee-management-service")
                .route(path("/api/admin/announcements/**")
                        .or(path("/api/employees/announcements/**"))
                        .or(path("/api/employee/expenses/**"))
                        .or(path("/api/admin/expenses/**"))
                        .or(path("/api/manager/expenses/**"))
                        .or(path("/api/ai/**"))
                        .or(path("/api/chat/**"))
                        .or(path("/api/admin/dashboard/**"))
                        .or(path("/api/manager/team/**")), http())
                .filter(lb("EMPLOYEE-MANAGEMENT-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> employeeMgmtWsRoute() {
        return route("employee-management-ws")
                .route(path("/ws/**"), http())
                .filter(lb("EMPLOYEE-MANAGEMENT-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> notificationServiceRoute() {
        return route("notification-service")
                .route(path("/api/notifications/**"), http())
                .filter(lb("NOTIFICATION-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> reportingServiceRoute() {
        return route("reporting-service")
                .route(path("/api/admin/reports/**"), http())
                .filter(lb("REPORTING-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return route("user-service")
                .route(path("/api/auth/**")
                        .or(path("/api/employees/me/**"))
                        .or(path("/api/employees/me"))
                        .or(path("/api/employees/dashboard"))
                        .or(path("/api/employees/directory/**"))
                        .or(path("/api/admin/employees/**"))
                        .or(path("/api/admin/departments/**"))
                        .or(path("/api/admin/designations/**"))
                        .or(path("/api/admin/ip-access/**"))
                        .or(path("/api/admin/activity-logs/**")), http())
                .filter(lb("USER-SERVICE"))
                .build();
    }
}
