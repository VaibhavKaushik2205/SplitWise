package com.project.splitwise.kafka;

public interface AsyncClient {

    void dispatchRequest(AsyncRequest asyncRequest);

    void dispatchReply(AsyncReply asyncReply);

    void dispatchRequestWithoutHandling(AsyncRequest asyncRequest);

    void dispatchReplyWithoutHandling(AsyncReply asyncReply);
}