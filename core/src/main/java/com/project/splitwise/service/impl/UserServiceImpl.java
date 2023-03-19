package com.project.splitwise.service.impl;

import com.project.splitwise.Utils.Utils;
import com.project.splitwise.contract.UserRequest;
import com.project.splitwise.entity.User;
import com.project.splitwise.mapper.UserMapper;
import com.project.splitwise.repository.UserRepository;
import com.project.splitwise.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(UserRequest userRequest) {
        return userRepository.findByReferenceId(userRequest.getReferenceId())
            .map(user -> updateUserDetails(user, userRequest))
            .orElseGet(() -> createNewUser(userRequest));
    }

    @Override
    public User updateUser(UserRequest userRequest) {
        return null;
    }

    @Override
    public Optional<User> findUserByPhoneNumberAndSource(String phoneNumber, String source) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<User> findUserByEmailAndSource(String email, String source) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    private User updateUserDetails(User user, UserRequest request) {
        log.info("updating user details as per request for user: {}, referenceId: {}",
            user.getName(), user.getReferenceId());
        updateExistingEntity(user, request);
        return save(user);
    }

    private User createNewUser(UserRequest userRequest) {
//        TODO : Add validations
        log.info("Creating new user with number: {}, and email: {}",
            userRequest.getPhoneNumber(), userRequest.getEmail());
        User newUser = UserMapper.map(userRequest);
        return save(newUser);
    }


    private void updateExistingEntity(User user, UserRequest userRequest) {
        Utils.setIfNotNull(user::setReferenceId, userRequest.getReferenceId());
        Utils.setIfNotNull(user::setName, userRequest.getName());
        Utils.setIfNotNull(user::setPhoneNumber, userRequest.getPhoneNumber());
        Utils.setIfNotNull(user::setEmail, userRequest.getEmail());
    }
}
