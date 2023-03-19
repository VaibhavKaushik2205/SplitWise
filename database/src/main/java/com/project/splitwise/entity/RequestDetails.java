package com.project.splitwise.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.splitwise.enums.ProcessingStatus;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "request_details")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDetails extends BaseEntity {

    UUID requestId;
    String requestType;
    String replyTopic;
    @Enumerated(EnumType.STRING)
    ProcessingStatus status;
    String payload;
}
