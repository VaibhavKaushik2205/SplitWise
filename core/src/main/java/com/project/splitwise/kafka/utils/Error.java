package com.project.splitwise.kafka.utils;

import java.util.Map;
import lombok.NonNull;

public class Error {

    private @NonNull String code;
    private @NonNull String description;
    private Map<String, Object> resources;

    public Error() {
    }

    public Error(final String code, final String description, final Map<String, Object> resources) {
        if (code == null) {
            throw new NullPointerException("code is marked non-null but is null");
        } else if (description == null) {
            throw new NullPointerException("description is marked non-null but is null");
        } else {
            this.code = code;
            this.description = description;
            this.resources = resources;
        }
    }

    public Error(final String code, final String description) {
        if (code == null) {
            throw new NullPointerException("code is marked non-null but is null");
        } else if (description == null) {
            throw new NullPointerException("description is marked non-null but is null");
        } else {
            this.code = code;
            this.description = description;
        }
    }

    public @NonNull String getCode() {
        return this.code;
    }

    public @NonNull String getDescription() {
        return this.description;
    }

    public Map<String, Object> getResources() {
        return this.resources;
    }
}
