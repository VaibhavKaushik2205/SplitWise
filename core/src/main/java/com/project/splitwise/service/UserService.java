package com.project.splitwise.service;

import com.project.splitwise.contract.UserRequest;
import com.project.splitwise.entity.User;
import java.util.Optional;

public interface UserService {

    User createUser (UserRequest userRequest);

    User updateUser (UserRequest userRequest);

    Optional<User> findUserByPhoneNumberAndSource(String phoneNumber, String source);

    Optional<User> findUserByEmailAndSource(String email, String source);

    User save(User user);
}
