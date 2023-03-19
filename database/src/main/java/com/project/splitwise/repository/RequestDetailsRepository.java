package com.project.splitwise.repository;

import com.project.splitwise.entity.RequestDetails;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestDetailsRepository extends JpaRepository<RequestDetails, Long> {

    Optional<RequestDetails> findByRequestIdAndRequestType(UUID requestId, String requestType);
}
