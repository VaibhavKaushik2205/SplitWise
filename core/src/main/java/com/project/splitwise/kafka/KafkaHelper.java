package com.project.splitwise.kafka;

import com.project.splitwise.constants.StringConstants;
import com.project.splitwise.entity.RequestDetails;
import com.project.splitwise.enums.ProcessingStatus;
import com.project.splitwise.kafka.utils.Error;
import com.project.splitwise.kafka.utils.Errors;
import com.project.splitwise.service.RequestDetailsService;
import com.project.splitwise.validators.DataValidator;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class KafkaHelper {

    private final AsyncClient asyncClient;
    private final RequestDetailsService requestDetailsService;

    public void buildAndDispatchAsyncRequest(String correlationId, String requestType, String payload,
        String submissionTopic, String replyTopic) {
        AsyncRequest asyncRequest = AsyncRequest.builder()
            .correlationId(correlationId)
            .jsonData(payload)
            .requestType(requestType)
            .submissionTopic(submissionTopic)
            .replyTopic(replyTopic)
            .build();
        asyncClient.dispatchRequestWithoutHandling(asyncRequest);
    }

    public void buildAndDispatchAsyncReply(String correlationId, String requestType, String payload,
        String submissionTopic, AsyncReplyStatus status, Errors errors) {
        AsyncReply asyncReply = AsyncReply.builder()
            .correlationId(correlationId)
            .jsonData(payload)
            .requestType(requestType)
            .submissionTopic(submissionTopic)
            .status(status)
            .errors(errors)
            .build();
        asyncClient.dispatchReplyWithoutHandling(asyncReply);
    }

    public boolean isRequestValid(RequestDetails requestDetail, long requestTimeOut) {
        if (!ProcessingStatus.PENDING.equals(requestDetail.getStatus())) {
            return false;
        }
        if (requestDetail.getUpdatedAt().isBefore(LocalDateTime.now().minusMinutes(requestTimeOut))) {
            buildAndDispatchAsyncReply(requestDetail.getRequestId().toString(),
                requestDetail.getRequestType(), null, requestDetail.getReplyTopic(),
                getAsyncReplyStatus(requestDetail.getStatus()),
                new Errors(List.of(new Error(HttpStatus.REQUEST_TIMEOUT.toString(),
                    StringConstants.Errors.REQUEST_TIMED_OUT))));
            requestDetailsService.updateStatus(requestDetail, ProcessingStatus.FAILED);
            return false;
        }
        return true;
    }

    public Errors getErrors(Object o) {
        List<String> violations = DataValidator.getViolations(o);
        Errors errors = new Errors();
        for (String violation : violations) {
            errors.getErrors().add(new Error(HttpStatus.BAD_REQUEST.name(), violation));
        }
        return errors;
    }


    private AsyncReplyStatus getAsyncReplyStatus(ProcessingStatus status) {
        return ProcessingStatus.SUCCESS.equals(status) ? AsyncReplyStatus.SUCCESS :
            AsyncReplyStatus.FAILURE;
    }
}