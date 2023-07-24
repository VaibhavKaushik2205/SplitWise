package com.project.splitwise.service;

import com.project.splitwise.entity.Expense;

import java.util.Optional;

public interface ExpenseService {

    Expense save(Expense expense);

    Optional<Expense> findById(Long expenseId);
}
