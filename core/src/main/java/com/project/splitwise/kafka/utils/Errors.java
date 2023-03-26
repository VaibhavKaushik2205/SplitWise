package com.project.splitwise.kafka.utils;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Errors {
    List<Error> errors = new ArrayList();

    public Errors(final List<Error> errors) {
        this.errors = errors;
    }

    public Errors() {
    }
}
