package com.project.splitwise.contract.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.splitwise.enums.SplitType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpenseRequest {

    @NotBlank
    String payingUser;
    double amount;
    @NotNull
    SplitType splitType;
    @Valid
    List<SplitRequest> userSplits;
    @Valid
    ExpenseMetadata expenseMetadata;
    String groupReferenceId;
}
