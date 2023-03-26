package com.project.splitwise.service;

import com.project.splitwise.entity.RequestDetails;
import com.project.splitwise.enums.ProcessingStatus;
import java.util.Optional;
import java.util.UUID;

public interface RequestDetailsService {

    RequestDetails create(UUID requestId, String requestType, String payload,
        String replyTopic);

    RequestDetails save(RequestDetails requestDetails);

    Optional<RequestDetails> fetchByRequestIdAndRequestType(UUID requestId, String requestType);

    RequestDetails fetchOrCreateByRequestIdAndRequestType(UUID requestId, String requestType,
        String payload, String replyTopic);

    void updateStatus(RequestDetails requestDetails, ProcessingStatus status);

    void updateRequestStatus(UUID requestId, String type, ProcessingStatus status);

}
