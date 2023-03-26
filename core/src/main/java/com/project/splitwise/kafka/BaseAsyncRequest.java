package com.project.splitwise.kafka;

public interface BaseAsyncRequest {

    String getCorrelationId();

    String getRequestType();
}
