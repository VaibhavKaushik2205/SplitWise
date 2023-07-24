package com.project.splitwise.service.impl;

import com.project.splitwise.utils.Utils;
import com.project.splitwise.constants.StringConstants.Errors;
import com.project.splitwise.contract.request.UserRequest;
import com.project.splitwise.contract.response.UserDetailsResponse;
import com.project.splitwise.entity.User;
import com.project.splitwise.exception.NotFoundException;
import com.project.splitwise.mapper.MapperUtil;
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
    @Transactional
    public UserDetailsResponse createUser(UserRequest userRequest) {
        if (Objects.nonNull(userRequest.getReferenceId())) {
            return userRepository.findByReferenceId(userRequest.getReferenceId())
                .map(user -> updateUserDetails(user, userRequest))
                .orElseGet(() -> createNewUser(userRequest));
        }
        return createNewUser(userRequest);
    }

    @Override
    @Transactional
    public UserDetailsResponse updateUser(String referenceId, UserRequest userRequest) {
        return userRepository.findByReferenceId(referenceId)
            .map(user -> updateUserDetails(user, userRequest))
            .orElseThrow(() -> new NotFoundException(Errors.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND));
    }

    @Override
    public Optional<User> findUserByReferenceId(String referenceId) {
        return userRepository.findByReferenceId(referenceId);
    }

    @Override
    public Optional<UserDetailsResponse> findUserByPhoneNumberAndSource(String phoneNumber, String source) {
        return userRepository.findByPhoneNumber(phoneNumber)
            .map(MapperUtil::map);
    }

    @Override
    public Optional<UserDetailsResponse> findUserByEmailAndSource(String email, String source) {
        return userRepository.findByEmail(email)
            .map(MapperUtil::map);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    private UserDetailsResponse updateUserDetails(User user, UserRequest request) {
        log.info("updating user details as per request for user: {}, referenceId: {}",
            user.getName(), user.getReferenceId());
        updateExistingUser(user, request);
        return MapperUtil.map(save(user));
    }

    private UserDetailsResponse createNewUser(UserRequest userRequest) {
        log.info("Creating new user with number: {}, and email: {}",
            userRequest.getPhoneNumber(), userRequest.getEmail());
        User newUser = MapperUtil.map(userRequest);
        return MapperUtil.map(save(newUser));
    }


    private void updateExistingUser(User user, UserRequest userRequest) {
        Utils.setIfNotNull(user::setReferenceId, userRequest.getReferenceId());
        Utils.setIfNotNull(user::setName, userRequest.getName());
        Utils.setIfNotNull(user::setPhoneNumber, userRequest.getPhoneNumber());
        Utils.setIfNotNull(user::setEmail, userRequest.getEmail());
    }
}
