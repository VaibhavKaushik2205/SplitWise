package com.project.splitwise.service.impl;

import com.project.splitwise.Utils.CollectionUtil;
import com.project.splitwise.Utils.Utils;
import com.project.splitwise.constants.StringConstants.Errors;
import com.project.splitwise.contract.request.SplitGroupRequest;
import com.project.splitwise.entity.SplitGroup;
import com.project.splitwise.entity.User;
import com.project.splitwise.exception.NotFoundException;
import com.project.splitwise.repository.SplitGroupRepository;
import com.project.splitwise.service.SplitGroupService;
import com.project.splitwise.service.UserService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SplitGroupServiceImpl implements SplitGroupService {

    private final SplitGroupRepository splitGroupRepository;
    private final UserService userService;

    @Override
    public SplitGroup createGroup(SplitGroupRequest groupRequest) {
        SplitGroup splitGroup = createNewGroup(groupRequest);
        return save(splitGroup);
    }

    @Override
    @Transactional
    public SplitGroup updateGroup(String groupReferenceId, SplitGroupRequest groupRequest) {
        return splitGroupRepository.findSplitGroupByGroupReferenceId(groupReferenceId)
            .map(splitGroup -> updateSplitGroup(splitGroup, groupRequest))
            .orElseThrow(() -> new NotFoundException(Errors.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public Optional<SplitGroup> fetchGroupByReferenceId(String groupReferenceId) {
        return splitGroupRepository.findSplitGroupByGroupReferenceId(groupReferenceId);
    }

    @Override
    public SplitGroup save(SplitGroup splitGroup) {
        return splitGroupRepository.save(splitGroup);
    }

    private SplitGroup updateSplitGroup(SplitGroup splitGroup, SplitGroupRequest groupRequest) {
        Utils.setIfNotNull(splitGroup::setNameOfGroup, groupRequest.getNameOfGroup());
        addGroupMembers(splitGroup, groupRequest.getMembers());
        return splitGroup;
    }

    private SplitGroup createNewGroup(SplitGroupRequest groupRequest) {
        SplitGroup splitGroup = SplitGroup.builder()
            .groupReferenceId(UUID.randomUUID().toString())
            .nameOfGroup(groupRequest.getNameOfGroup())
            .members(Collections.emptyList())
            .build();
        save(splitGroup);
        addGroupMembers(splitGroup, groupRequest.getMembers());
        return splitGroup;
    }

    private void addGroupMembers(SplitGroup splitGroup, List<String> members) {
        members.stream()
            .filter(CollectionUtil.distinctByKey(userReferenceId -> userReferenceId))
            .forEach(userReferenceId ->
                userService.findUserByReferenceId(userReferenceId)
                    .ifPresent(user -> addUserToGroup(user, splitGroup)));
    }

    @Transactional
    private void addUserToGroup(User user, SplitGroup splitGroup) {
        // check if group is already added to user
        boolean isGroupPresent =
            user.getUserGroups().stream()
                .anyMatch(userGroup ->
                    userGroup.getGroupReferenceId().equals(splitGroup.getGroupReferenceId()));

        if (isGroupPresent) {
            log.info("User already member of group: {}", splitGroup.getGroupReferenceId());
            return;
        }
        user.addGroup(splitGroup);
        userService.save(user);
    }

}
