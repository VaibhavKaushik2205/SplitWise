package com.project.splitwise.service;

import com.project.splitwise.contract.request.SplitGroupRequest;
import com.project.splitwise.contract.response.GroupDetailsResponse;
import com.project.splitwise.entity.SplitGroup;
import java.util.Optional;

public interface SplitGroupService {

    SplitGroup createGroup(SplitGroupRequest groupRequest);

    SplitGroup updateGroup(String groupReferenceId, SplitGroupRequest groupRequest);

    Optional<SplitGroup> findByReferenceId(String groupReferenceId);

    Optional<GroupDetailsResponse> findGroupByReferenceId(String groupReferenceId);

    SplitGroup save(SplitGroup splitGroup);
}
