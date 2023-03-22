package com.project.splitwise.service.impl;

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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
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
        addGroupMembers2(splitGroup, groupRequest.getMembers());
        return splitGroup;
    }

    private SplitGroup createNewGroup(SplitGroupRequest groupRequest) {
        SplitGroup splitGroup = SplitGroup.builder()
            .groupReferenceId(UUID.randomUUID().toString())
            .nameOfGroup(groupRequest.getNameOfGroup())
            .members(Collections.emptyList())
            .build();
        save(splitGroup);
        addGroupMembers2(splitGroup, groupRequest.getMembers());
        return splitGroup;
    }

    private void addGroupMembers(SplitGroup splitGroup, List<String> members) {
        for (String userReferenceId : members) {
            userService.findUserByReferenceId(userReferenceId)
                .ifPresent(user -> addUserToGroup(user, splitGroup));
        }
    }

    private void addGroupMembers2(SplitGroup splitGroup, List<String> members) {
        members.stream()
            .filter(distinctByKey(userId -> userId))
            .forEach(userReferenceId ->
                userService.findUserByReferenceId(userReferenceId)
                    .ifPresent(user -> addUserToGroup2(user, splitGroup)));
    }

    private void addUserToGroup(User user, SplitGroup splitGroup) {
        user.getUserGroups().add(splitGroup);
        userService.save(user);

//        splitGroup.getMembers().add(user);
    }

    @Transactional
    private void addUserToGroup2(User user, SplitGroup splitGroup) {
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

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
