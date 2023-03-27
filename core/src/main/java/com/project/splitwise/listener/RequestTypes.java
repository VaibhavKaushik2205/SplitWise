package com.project.splitwise.listener;

import org.apache.commons.lang3.StringUtils;

public enum RequestTypes {

    INITIATE_PAYMENT,
    UNKNOWN;

    public static RequestTypes getInstance(String requestType) {
        if (StringUtils.isBlank(requestType)) {
            return RequestTypes.UNKNOWN;
        }
        try {
            return RequestTypes.valueOf(requestType);
        } catch (IllegalArgumentException ie) {
            return RequestTypes.UNKNOWN;
        }
    }

}
