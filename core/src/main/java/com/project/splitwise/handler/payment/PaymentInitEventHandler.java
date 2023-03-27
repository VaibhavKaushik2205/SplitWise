package com.project.splitwise.handler.payment;

import com.project.splitwise.utils.JacksonUtils;
import com.project.splitwise.contract.request.InitiatePaymentRequest;
import com.project.splitwise.kafka.AsyncReplyStatus;
import com.project.splitwise.kafka.KafkaHelper;
import com.project.splitwise.listener.RequestTypes;
import com.project.splitwise.validators.DataValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentInitEventHandler implements PaymentEventHandler {

    private final JacksonUtils jacksonUtils;
    private final KafkaHelper kafkaHelper;

    @Override
    public RequestTypes getPaymentHandlerRequestType() {
        return RequestTypes.INITIATE_PAYMENT;
    }

    @Override
    public void handle(String correlationId, RequestTypes requestType, String payload,
        String replyTopic) {
        InitiatePaymentRequest request = jacksonUtils.stringToObject(payload, InitiatePaymentRequest.class);
        if (DataValidator.validate(request)) {
            log.info("Initiating payment btw: {} and {}", request.getSenderUserId(), request.getSenderUserId());
            // call payment service to init payment
        } else {
            log.error("Trust validation failed while handling request for payload: {}", payload);
            kafkaHelper.dispatchAsyncReplyWithTracker(correlationId, requestType.name(), null,
                AsyncReplyStatus.FAILURE, kafkaHelper.getErrors(request));
        }
    }

}
