package com.project.splitwise.service.impl;

import com.project.splitwise.entity.RequestDetails;
import com.project.splitwise.enums.ProcessingStatus;
import com.project.splitwise.repository.RequestDetailsRepository;
import com.project.splitwise.service.RequestDetailsService;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestDetailsServiceImpl implements RequestDetailsService {

    private final RequestDetailsRepository requestDetailsRepository;

    @Override
    @Transactional
    public RequestDetails create(UUID requestId, String requestType, String payload,
        String replyTopic) {
        log.info("creating requestDetails for with requestId: {} and requestType: {}",
            requestId, requestType);
        RequestDetails requestDetails = RequestDetails.builder()
            .requestId(requestId)
            .requestType(requestType)
            .payload(payload)
            .replyTopic(replyTopic)
            .status(ProcessingStatus.PENDING)
            .build();
        return requestDetailsRepository.save(requestDetails);
    }

    @Override
    public RequestDetails save(RequestDetails requestDetails) {
        return requestDetailsRepository.save(requestDetails);
    }

    @Override
    public Optional<RequestDetails> fetchByRequestIdAndRequestType(UUID requestId, String requestType) {
        return requestDetailsRepository.findByRequestIdAndRequestType(requestId, requestType);
    }

    @Override
    public RequestDetails fetchOrCreateByRequestIdAndRequestType(UUID requestId, String requestType,
        String payload, String replyTopic) {
        return requestDetailsRepository.findByRequestIdAndRequestType(requestId, requestType)
            .orElseGet(() -> create(requestId, requestType, payload, replyTopic));
    }

    @Override
    @Transactional
    public void updateStatus(RequestDetails requestDetails, ProcessingStatus status) {
        requestDetails.setStatus(status);
        save(requestDetails);
    }

    @Override
    public void updateRequestStatus(UUID requestId, String type, ProcessingStatus status) {
        requestDetailsRepository.findByRequestIdAndRequestType(requestId, type)
            .ifPresent(requestDetail -> {
                requestDetail.setStatus(status);
                save(requestDetail);
            });
    }
}
