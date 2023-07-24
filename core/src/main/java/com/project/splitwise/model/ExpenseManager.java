package com.project.splitwise.model;

import com.project.splitwise.constants.StringConstants;
import com.project.splitwise.contract.request.ExpenseRequest;
import com.project.splitwise.contract.request.SplitRequest;
import com.project.splitwise.entity.Expense;
import com.project.splitwise.entity.Split;
import com.project.splitwise.entity.SplitGroup;
import com.project.splitwise.entity.User;
import com.project.splitwise.exception.InvalidSplitException;
import com.project.splitwise.exception.NotFoundException;
import com.project.splitwise.service.ExpenseService;
import com.project.splitwise.service.SplitGroupService;
import com.project.splitwise.service.SplitService;
import com.project.splitwise.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseManager {

    private final UserService userService;
    private final SplitGroupService groupService ;
    private final ExpenseService expenseService;
    private final SplitService splitService;

    public Expense createNewExpense(String referenceId, ExpenseRequest expenseRequest) {
        validateUser(referenceId);
        validateRequest(expenseRequest);

        // Create New expense
        Expense expense = expenseService.save(new Expense());

        return createOrUpdateExpense(expense, expenseRequest);
    }

    public Expense updateExpense(String referenceId, Long expenseId, ExpenseRequest expenseRequest) {
        validateUser(referenceId);
        validateRequest(expenseRequest);
        Expense savedExpense = expenseService.findById(expenseId)
            .orElseThrow(() -> new NotFoundException(StringConstants.Errors.EXPENSE_NOT_FOUND,
                HttpStatus.NOT_FOUND));

        return createOrUpdateExpense(savedExpense, expenseRequest);
    }

    public Optional<Expense> getExpenseDetails(Long expenseId) {
        return expenseService.findById(expenseId);
    }

    private Expense createOrUpdateExpense(Expense savedExpense, ExpenseRequest expenseRequest) {

        // Update amount and paying user
        savedExpense.setAmount(expenseRequest.getAmount());
        User payingUser = userService.findUserByReferenceId(expenseRequest.getPayingUser()).get();
        savedExpense.setPaidBy(payingUser);

        List<SplitRequest> splits = expenseRequest.getUserSplits();
        List<Split> userSplits = new ArrayList<>();

        long totalSplits = splits.size();
        switch (expenseRequest.getSplitType()) {
            case EQUAL_SPLIT:
                double splitAmount = ((double) Math.round(expenseRequest.getAmount()*100 / totalSplits)) / 100.0;
                userSplits = splits.stream().map(split -> createSplit(savedExpense, splitAmount, split)).collect(Collectors.toList());
                break;
            case PERCENT_SPLIT:
                userSplits = splits.stream().map(split -> {
                    double userSplit = (expenseRequest.getAmount() * split.getPercentSplit()) / 100.0;
                    return createSplit(savedExpense, userSplit, split);
                }).collect(Collectors.toList());
                break;
            case EXACT_SPLIT:
                userSplits = splits.stream().map(split -> createSplit(savedExpense, split.getAmount(), split)).collect(Collectors.toList());
                break;
        }

        // update users
        userSplits.forEach(payingUser::addOrUpdateUserSplits);

        // if groupId is present, update group
        if (Objects.nonNull(expenseRequest.getGroupReferenceId())) {
            updateSplitGroup(savedExpense, expenseRequest.getGroupReferenceId());
        }

        // updateExpense
        savedExpense.setSplits(userSplits);

        return expenseService.save(savedExpense);
    }

    private Split createSplit(Expense expense, Double splitAmount, SplitRequest split) {
        Split newSplit = Split.builder()
            .expense(expense)
            .amount(splitAmount)
            .userReferenceId(split.getOwingUserId())
            .userName(split.getOwingUserName())
            .build();
        return splitService.save(newSplit);
    }

    private void updateSplitGroup(Expense expense, String referenceId) {
        SplitGroup group = groupService.findByReferenceId(referenceId)
            .orElseThrow(() -> new NotFoundException(StringConstants.Errors.GROUP_NOT_FOUND, HttpStatus.BAD_REQUEST));
//        expense.setGroup(group);
        group.addOrUpdateExpense(expense);
        groupService.save(group);
    }

    private void validateRequest(ExpenseRequest expenseRequest) {
        validateUser(expenseRequest.getPayingUser());
        validateSplits(expenseRequest);
    }

    private void validateSplits(ExpenseRequest expenseRequest) {
        // validate split based on Split Type
        log.info("SplitType: {}", expenseRequest.getSplitType());
        switch (expenseRequest.getSplitType()) {
            case EXACT_SPLIT:
                // check if sum off all splits is same as net amount
                double splitSum = expenseRequest.getUserSplits().stream()
                    .mapToDouble(SplitRequest::getAmount).sum();
                if (splitSum != expenseRequest.getAmount()) {
                    throw new InvalidSplitException(StringConstants.Errors.INCORRECT_SPLIT_DATA,
                        HttpStatus.BAD_REQUEST);
                }
                break;
            case PERCENT_SPLIT:
                // check if split percentages are equal to 100
                double percentSum = expenseRequest.getUserSplits().stream()
                    .mapToDouble(SplitRequest::getPercentSplit).sum();
                if (percentSum != 100) { // add upper round in percentSum
                    throw new InvalidSplitException(StringConstants.Errors.INCORRECT_SPLIT_DATA,
                        HttpStatus.BAD_REQUEST);
                }
        }
    }

    private void validateUser(String referenceId) {
        userService.findUserByReferenceId(referenceId)
            .orElseThrow(() -> new NotFoundException(StringConstants.Errors.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND));
    }
}
