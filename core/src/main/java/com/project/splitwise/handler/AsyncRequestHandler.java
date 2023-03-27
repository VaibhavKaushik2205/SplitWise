package com.project.splitwise.handler;


import com.project.splitwise.listener.RequestTypes;

public interface AsyncRequestHandler {

    void handle(String correlationId, RequestTypes requestType, String payload, String replyTopic) throws Exception;

}