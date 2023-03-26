package com.project.splitwise.validators;

import com.project.splitwise.constants.StringConstants.Errors;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Slf4j
public class DataValidator {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    public static boolean validate(Object object) {
        if (Objects.isNull(object)) {
            return false;
        }
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if (!CollectionUtils.isEmpty(violations)) {
            for (ConstraintViolation<Object> violation : violations) {
                log.warn("Validation failed as property : {} has value : {}", violation.getPropertyPath(), violation.getLeafBean());
            }
            return false;
        }
        return true;
    }

    public static List<String> getViolations(Object object) {
        List<String> errors = new ArrayList<>();
        if (Objects.isNull(object)) {
            errors.add("Validation failed as required object is null");
            return errors;
        }
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if (!CollectionUtils.isEmpty(violations)) {
            for (ConstraintViolation<Object> violation : violations) {
                log.warn("Validation failed as property : {} has value : {}",
                    violation.getPropertyPath(), violation.getMessage());
                errors.add(String.format(Errors.DATA_VALIDATION_FAILED,
                    violation.getPropertyPath(), violation.getLeafBean()));
            }
            return errors;
        }
        return Collections.emptyList();
    }
}