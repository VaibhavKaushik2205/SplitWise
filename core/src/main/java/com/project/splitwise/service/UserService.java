package com.project.splitwise.service;

import com.project.splitwise.contract.request.UserRequest;
import com.project.splitwise.contract.response.UserDetailsResponse;
import com.project.splitwise.entity.User;
import java.util.Optional;

public interface UserService {

    UserDetailsResponse createUser(UserRequest userRequest);

    UserDetailsResponse updateUser(String referenceId, UserRequest userRequest);

    Optional<User> findUserByReferenceId(String referenceId);

    Optional<UserDetailsResponse> findUserByPhoneNumberAndSource(String phoneNumber, String source);

    Optional<UserDetailsResponse> findUserByEmailAndSource(String email, String source);

    User save(User user);
}
