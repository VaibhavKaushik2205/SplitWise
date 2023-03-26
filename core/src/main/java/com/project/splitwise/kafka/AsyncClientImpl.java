package com.project.splitwise.kafka;

import com.project.splitwise.Utils.JacksonUtils;
import com.project.splitwise.constants.Constants.KafkaRequestHeaders;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AsyncClientImpl implements AsyncClient {

    @Value("${producer.timeout.in.seconds:10}")
    private int timeoutInSeconds;
    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private final JacksonUtils jacksonUtils;

    @Override
    public void dispatchRequest(AsyncRequest asyncRequest) {
        Message<String> message = this.requestMessage(asyncRequest);
        try {
            sendEvents(message);
        } catch (Exception exception) {
            log.error("Exception:{} caused while dispatching request: {}", exception.getMessage(),
                asyncRequest.getRequestType());
        }
    }

    @Override
    public void dispatchRequestWithoutHandling(AsyncRequest asyncRequest) {
        Message<String> message = this.requestMessage(asyncRequest);
        try {
            sendEvents(message);
        } catch (Exception exception) {
            log.error("Exception:{} caused while dispatching request: {}", exception.getMessage(),
                asyncRequest.getRequestType());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void dispatchReply(AsyncReply asyncReply) {
        Message<String> message = this.replyMessage(asyncReply);
        try {
            sendEvents(message);
        } catch (Exception exception) {
            log.error("Exception:{} caused while dispatching reply: {}", exception.getMessage(),
                asyncReply.getRequestType());
        }
    }

    @Override
    public void dispatchReplyWithoutHandling(AsyncReply asyncReply) {
        Message<String> message = this.replyMessage(asyncReply);
        try {
            sendEvents(message);
        } catch (Exception exception) {
            log.error("Exception:{} caused while dispatching reply: {}", exception.getMessage(),
                asyncReply.getRequestType());
            throw new RuntimeException(exception.getMessage());
        }
    }

    private void sendEvents(Message<String> message) {

        try {
            //log.info("sending message: {} to topic: {}", message, message.getHeaders().get(KafkaHeaders.TOPIC));
            kafkaTemplate.send(message).get(this.timeoutInSeconds, TimeUnit.SECONDS);
            //log.info("Successfully dispatched for requestType {} and topic {}", message.getHeaders().get(KafkaRequestHeaders.REQUEST_TYPE),
            //   message.getHeaders().get(KafkaHeaders.TOPIC));
        } catch (InterruptedException | TimeoutException ex1) {
            log.error("Failed while dispatching for requestType {} and topic {}", message.getHeaders().get(KafkaRequestHeaders.REQUEST_TYPE),
                message.getHeaders().get(KafkaHeaders.TOPIC));
            throw new RuntimeException("Timed out while sending message");
        } catch (ExecutionException ex2) {
            log.error("Failed while dispatching for requestType {} and topic {}", message.getHeaders().get(KafkaRequestHeaders.REQUEST_TYPE),
                message.getHeaders().get(KafkaHeaders.TOPIC));
            throw new RuntimeException("Failed while sending message");
        }
    }

    private Message<String> requestMessage(AsyncRequest asyncRequest) {
        String payload = asyncRequest.getJsonData();
        MessageBuilder<String> builder = MessageBuilder.withPayload(payload)
            .setHeader(KafkaRequestHeaders.CORRELATION_ID, this.correlationId(asyncRequest))
            .setHeader(KafkaRequestHeaders.TOPIC, asyncRequest.getSubmissionTopic())
            .setHeader(KafkaRequestHeaders.MESSAGE_KEY, this.messageKey(asyncRequest))
            .setHeader(KafkaRequestHeaders.REPLY_TOPIC, asyncRequest.getReplyTopic())
            .setHeader(KafkaRequestHeaders.REQUEST_TYPE, asyncRequest.getRequestType())
            .setHeader(KafkaRequestHeaders.IDEMPOTENCY_KEY, asyncRequest.getIdempotencyKey())
            .setHeader(KafkaRequestHeaders.SOURCE_ID, asyncRequest.getSourceId())
            .setHeader(KafkaRequestHeaders.CUSTOMER_ID, asyncRequest.getCustomerReferenceId());;
        return builder.build();
    }

    private Message<String> replyMessage(AsyncReply asyncReply) {
        String payload = this.payload(asyncReply);
        String messageHeader = asyncReply.getMessage() == null ? " " : asyncReply.getMessage();
        String status = Optional.ofNullable(asyncReply.getStatus()).orElse(AsyncReplyStatus.SUCCESS).name();
        MessageBuilder<String> builder = MessageBuilder.withPayload(payload)
            .setHeader(KafkaRequestHeaders.CORRELATION_ID, asyncReply.getCorrelationId())
            .setHeader(KafkaRequestHeaders.TOPIC, asyncReply.getSubmissionTopic())
            .setHeader(KafkaRequestHeaders.MESSAGE_KEY, asyncReply.getCorrelationId())
            .setHeader(KafkaRequestHeaders.REPLY_STATUS, status)
            .setHeader(KafkaRequestHeaders.REPLY_MESSAGE, messageHeader)
            .setHeader(KafkaRequestHeaders.REQUEST_TYPE, asyncReply.getRequestType())
            .setHeader(KafkaRequestHeaders.CUSTOMER_ID, asyncReply.getCustomerReferenceId());
        return builder.build();
    }

    private String payload(AsyncReply asyncReply) {
        return asyncReply.isSuccess() ? asyncReply.getJsonData() : this.jacksonUtils.objectToString(asyncReply.getErrors());
    }

    private String messageKey(AsyncRequest asyncRequest) {
        return StringUtils.isBlank(asyncRequest.getMessageKey()) ? asyncRequest.getCorrelationId() : asyncRequest.getMessageKey();
    }

    private String correlationId(AsyncRequest asyncRequest) {
        if (StringUtils.isBlank(asyncRequest.getCorrelationId()) && StringUtils.isBlank(asyncRequest.getIdempotencyKey())) {
            return UUID.randomUUID().toString();
        } else if (StringUtils.isBlank(asyncRequest.getCorrelationId())) {
            return asyncRequest.getIdempotencyKey();
        }
        return asyncRequest.getCorrelationId();
    }
}
