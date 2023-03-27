package com.project.splitwise.utils;

import java.util.Objects;
import java.util.function.Consumer;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

@Log4j2
@UtilityClass
public class Utils {

    public  <T> void setIfNotNull(Consumer<T> setter, T value) {
        if (Objects.isNull(value) || (value instanceof String && StringUtils.isBlank((String) value))) {
            return;
        }
        setter.accept(value);
    }

    public int generateHashCode(Object... objects) {
        return Objects.hash(objects);
    }
}
