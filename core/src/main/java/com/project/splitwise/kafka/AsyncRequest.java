package com.project.splitwise.kafka;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AsyncRequest implements BaseAsyncRequest {

    private String correlationId;
    private String submissionTopic;
    private String jsonData;
    private String replyTopic;
    private String requestType;
    private String idempotencyKey;
    private String sourceId;
    private Map<String, String> customHeaders;
    @Nullable
    private String messageKey;
    private String customerReferenceId;

    public String toString() {
        String correlationId1 = this.getCorrelationId();
        return "AsyncRequest(correlationId=" + correlationId1 + ", submissionTopic=" + this.getSubmissionTopic() + ", jsonData=" + this.getJsonData()
            + ", replyTopic=" + this.getReplyTopic() + ", requestType=" + this.getRequestType() + ", idempotencyKey=" + this.getIdempotencyKey() + ", sourceId="
            + this.getSourceId() + ", customHeaders=" + this.getCustomHeaders() + ", messageKey=" + this.getMessageKey() + ")";
    }
}
