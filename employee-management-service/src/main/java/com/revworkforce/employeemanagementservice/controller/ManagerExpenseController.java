package com.revworkforce.employeemanagementservice.controller;

import com.revworkforce.employeemanagementservice.dto.ExpenseActionRequest;
import com.revworkforce.employeemanagementservice.model.Expense;
import com.revworkforce.employeemanagementservice.model.enums.ExpenseStatus;
import com.revworkforce.employeemanagementservice.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/expenses")
public class ManagerExpenseController {
    @Autowired private ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<Page<Expense>> getTeamExpenses(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(expenseService.getTeamExpenses(auth.getName(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "submittedDate"))));
    }

    @PatchMapping("/{id}/action")
    public ResponseEntity<Expense> actionExpense(
            Authentication auth,
            @PathVariable Integer id,
            @RequestBody ExpenseActionRequest request) {
        return ResponseEntity.ok(expenseService.managerAction(auth.getName(), id, request));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Expense>> getAllExpenses(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ExpenseStatus expenseStatus = null;
        if (status != null) {
            try { expenseStatus = ExpenseStatus.valueOf(status.toUpperCase()); } catch (Exception ignored) {}
        }
        return ResponseEntity.ok(expenseService.getAllExpenses(expenseStatus,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))));
    }

    @GetMapping("/finance-pending")
    public ResponseEntity<Page<Expense>> getFinancePending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(expenseService.getFinancePendingExpenses(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "managerActionDate"))));
    }

    @PatchMapping("/{id}/finance-action")
    public ResponseEntity<Expense> financeAction(
            Authentication auth,
            @PathVariable Integer id,
            @RequestBody ExpenseActionRequest request) {
        return ResponseEntity.ok(expenseService.financeAction(auth.getName(), id, request));
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<Resource> getExpenseReceipt(Authentication auth, @PathVariable Integer id) {
        ExpenseService.ReceiptFileData receipt = expenseService.getExpenseReceipt(auth.getName(), id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + receipt.fileName() + "\"")
                .contentType(MediaType.parseMediaType(receipt.contentType()))
                .body(receipt.resource());
    }
}

