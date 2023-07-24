package com.project.splitwise.controller;

import com.project.splitwise.contract.request.ExpenseRequest;
import com.project.splitwise.model.ExpenseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/expense")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ExpenseController {

    private final ExpenseManager expenseManager;

    @PostMapping("")
    public ResponseEntity<?> createExpense(
            @RequestParam("creatingUserId") String creatingUserId,
            @Valid @RequestBody ExpenseRequest expenseRequest) {
        log.info("create expense request received by user id: {}", creatingUserId);
        return ResponseEntity.ok(expenseManager.createNewExpense(creatingUserId, expenseRequest));
    }

    @PutMapping("/{expenseId}/update")
    public ResponseEntity<?> updateExpense(
            @PathVariable("expenseId") Long expenseId,
            @RequestParam("creatingUserId") String creatingUserId,
            @Valid @RequestBody ExpenseRequest expenseRequest) {
        log.info("update expense request received by user id: {} for expense: {}", creatingUserId, expenseId);
        return ResponseEntity.ok(expenseManager.updateExpense(creatingUserId, expenseId, expenseRequest));
    }

    @GetMapping("/details")
    public ResponseEntity<?> getExpenseDetails(@RequestParam("expenseId") Long expenseId) {
        log.info("[GET] request received for expense: {}", expenseId);
        return expenseManager.getExpenseDetails(expenseId)
            .map(expense -> new ResponseEntity<>(expense, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
