package com.project.splitwise.mapper;

import com.project.splitwise.contract.UserRequest;
import com.project.splitwise.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public User map(UserRequest userRequest) {
        return User.builder()
            .referenceId(userRequest.getReferenceId())
            .email(userRequest.getEmail())
            .phoneNumber(userRequest.getPhoneNumber())
            .name(userRequest.getName())
            .build();
    }
}
