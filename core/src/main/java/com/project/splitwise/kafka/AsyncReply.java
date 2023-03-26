package com.project.splitwise.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.splitwise.kafka.utils.Errors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(
    ignoreUnknown = true
)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
@Builder
@AllArgsConstructor
public class AsyncReply implements BaseAsyncRequest {

    private String correlationId;
    private String requestType;
    private AsyncReplyStatus status;
    private String message;
    private Errors errors;
    private String jsonData;
    private String submissionTopic;
    private String customerReferenceId;

    public AsyncReply() {
        this.errors = defaultErrors();
    }

    public static AsyncReply success(String correlationId, String requestType, String jsonData,
        String submissionTopic) {
        return builder().correlationId(correlationId).requestType(requestType)
            .status(AsyncReplyStatus.SUCCESS).submissionTopic(submissionTopic)
            .jsonData(jsonData).message(" ").build();
    }

    public static AsyncReply failure(String correlationId, String requestType, String message,
        Errors errors, String submissionTopic) {
        return builder().correlationId(correlationId).requestType(requestType)
            .status(AsyncReplyStatus.FAILURE).submissionTopic(submissionTopic).errors(errors)
            .message(message).build();
    }

    private static Errors defaultErrors() {
        return new Errors();
    }

    public boolean isSuccess() {
        return AsyncReplyStatus.SUCCESS.equals(this.status);
    }

    public String toString() {
        String correlationId1 = this.getCorrelationId();
        return "AsyncReply(correlationId=" + correlationId1 + ", requestType="
            + this.getRequestType() + ", status=" + this.getStatus() + ", message="
            + this.getMessage() + ", errors=" + this.getErrors() + ", jsonData="
            + this.getJsonData() + ", submissionTopic=" + this.getSubmissionTopic() + ")";
    }
}