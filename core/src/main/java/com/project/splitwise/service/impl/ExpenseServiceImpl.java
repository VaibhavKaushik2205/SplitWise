package com.project.splitwise.service.impl;

import com.project.splitwise.entity.Expense;
import com.project.splitwise.repository.ExpenseRepository;
import com.project.splitwise.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Override
    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public Optional<Expense> findById(Long expenseId) {
        return expenseRepository.findById(expenseId);
    }

}
