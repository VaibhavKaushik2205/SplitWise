package com.project.splitwise.service.impl;

import com.project.splitwise.Utils.Utils;
import com.project.splitwise.constants.StringConstants.Errors;
import com.project.splitwise.contract.UserRequest;
import com.project.splitwise.entity.User;
import com.project.splitwise.exception.BadRequestException;
import com.project.splitwise.exception.NotFoundException;
import com.project.splitwise.mapper.UserMapper;
import com.project.splitwise.repository.UserRepository;
import com.project.splitwise.service.UserService;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(UserRequest userRequest) {
        if (Objects.nonNull(userRequest.getReferenceId())) {
            return userRepository.findByReferenceId(userRequest.getReferenceId())
                .map(user -> updateUserDetails(user, userRequest))
                .orElseGet(() -> createNewUser(userRequest));
        }
        return createNewUser(userRequest);
    }

    @Override
    @Transactional
    public User updateUser(UserRequest userRequest) {
        if (Objects.isNull(userRequest.getReferenceId())) {
            throw new BadRequestException(Errors.USER_ID_MISSING, HttpStatus.BAD_REQUEST);
        }
        return userRepository.findByReferenceId(userRequest.getReferenceId())
            .map(user -> updateUser(userRequest))
            .orElseThrow(() -> new NotFoundException(Errors.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND));
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
