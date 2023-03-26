package com.project.splitwise.mapper;

import com.project.splitwise.contract.request.UserRequest;
import com.project.splitwise.contract.response.GroupDetailsResponse;
import com.project.splitwise.contract.response.GroupDetailsResponse.GroupMember;
import com.project.splitwise.contract.response.UserDetailsResponse;
import com.project.splitwise.entity.SplitGroup;
import com.project.splitwise.entity.User;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomMapper {

    public User map(UserRequest userRequest) {
        return User.builder()
            .referenceId(Objects.nonNull(userRequest.getReferenceId())
                ? userRequest.getReferenceId() : UUID.randomUUID().toString())
            .email(userRequest.getEmail())
            .phoneNumber(userRequest.getPhoneNumber())
            .name(userRequest.getName())
            .build();
    }

    public UserDetailsResponse map(User user) {
        return UserDetailsResponse.builder()
            .referenceId(user.getReferenceId())
            .name(user.getName())
            .email(user.getEmail())
            .phoneNumber(user.getPhoneNumber())
            .userGroups(user.getUserGroups().stream()
                .map(CustomMapper::map).collect(Collectors.toList()))
            .build();
    }

    public GroupDetailsResponse map(SplitGroup splitGroup) {
        return GroupDetailsResponse.builder()
            .nameOfGroup(splitGroup.getNameOfGroup())
            .members(splitGroup.getMembers().stream()
                .map(CustomMapper::addGroupMember).collect(Collectors.toList()))
            .build();
    }

    public GroupMember addGroupMember(User user) {
        return GroupMember.builder()
            .userReferenceId(user.getReferenceId())
            .name(user.getName())
            .phoneNumber(user.getPhoneNumber())
            .email(user.getEmail())
            .build();
    }
}
