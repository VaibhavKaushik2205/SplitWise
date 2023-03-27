package com.project.splitwise.listener.payment;

import com.project.splitwise.constants.Constants.KafkaRequestHeaders;
import com.project.splitwise.constants.StringConstants;
import com.project.splitwise.entity.RequestDetails;
import com.project.splitwise.enums.ProcessingStatus;
import com.project.splitwise.handler.AsyncRequestHandler;
import com.project.splitwise.handler.payment.PaymentEventHandler;
import com.project.splitwise.kafka.AsyncReplyStatus;
import com.project.splitwise.kafka.KafkaHelper;
import com.project.splitwise.kafka.utils.Error;
import com.project.splitwise.kafka.utils.Errors;
import com.project.splitwise.listener.AbstractListenerBase;
import com.project.splitwise.listener.RequestTypes;
import com.project.splitwise.service.RequestDetailsService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventListener extends AbstractListenerBase {

    private final List<PaymentEventHandler> requestHandlers;
    private final Map<RequestTypes, AsyncRequestHandler> requestTypeToHandlerMap = new HashMap<>();
    private final KafkaHelper kafkaHelper;
    private final RequestDetailsService requestDetailsService;

    @PostConstruct
    public void initRequestTypeToHandlerMap() {
        for (PaymentEventHandler handler : requestHandlers) {
            requestTypeToHandlerMap.put(handler.getPaymentHandlerRequestType(), handler);
        }
    }

    @KafkaListener(topics = "${payment.event.topic}", groupId = "${payment.event.consumer.group}")
    public void listen(@Header(KafkaRequestHeaders.CORRELATION_ID) String correlationId,
        @Header(KafkaRequestHeaders.REQUEST_TYPE) String requestType,
        @Header(KafkaRequestHeaders.REPLY_TOPIC) String replyTopic,
        @Payload String payload) throws Exception {
        log.info("Received request for payment event with id: {} request type: {}",
            correlationId, requestType);
        UUID requestId = UUID.fromString(correlationId);
        RequestDetails requestDetails =
            requestDetailsService.fetchOrCreateByRequestIdAndRequestType(requestId, requestType,
                payload, replyTopic);
        if (ProcessingStatus.PENDING.equals(requestDetails.getStatus())) {
            RequestTypes requestTypeEnum = RequestTypes.getInstance(requestType);
            AsyncRequestHandler handler = getHandler(requestTypeEnum);
            if (Objects.nonNull(handler)) {
                handler.handle(correlationId, requestTypeEnum, payload, replyTopic);
            } else {
                log.warn("Processing for request type: {} is not configured", requestType);
                kafkaHelper.dispatchAsyncReplyWithTracker(correlationId, requestType, null,
                    AsyncReplyStatus.FAILURE, new Errors(List.of(new Error(HttpStatus.BAD_REQUEST.toString(),
                        StringConstants.Errors.REQUEST_TYPE_NOT_CONFIGURED))));
            }
        } else {
            kafkaHelper.buildAndDispatchAsyncReply(correlationId,
                requestType, null, replyTopic, AsyncReplyStatus.FAILURE,
                new Errors(List.of(new Error(HttpStatus.BAD_REQUEST.toString(),
                    StringConstants.Errors.INVALID_REQUEST_ID))));
        }
    }

    private AsyncRequestHandler getHandler(RequestTypes requestType) {
        return requestTypeToHandlerMap.getOrDefault(requestType, null);
    }

}
