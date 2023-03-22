package com.project.splitwise.mapper;

import com.project.splitwise.contract.request.UserRequest;
import com.project.splitwise.entity.User;
import java.util.Objects;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public User map(UserRequest userRequest) {
        return User.builder()
            .referenceId(Objects.nonNull(userRequest.getReferenceId())
                ? userRequest.getReferenceId() : UUID.randomUUID().toString())
            .email(userRequest.getEmail())
            .phoneNumber(userRequest.getPhoneNumber())
            .name(userRequest.getName())
            .build();
    }
}
