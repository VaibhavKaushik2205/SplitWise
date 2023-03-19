package com.project.splitwise.controller;

import com.project.splitwise.contract.UserRequest;
import com.project.splitwise.service.UserGroupService;
import com.project.splitwise.service.UserService;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SplitWiseController {

    private final UserService userService;

    private final UserGroupService userGroupService;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("createCustomer request received for customer: {}", userRequest.getReferenceId());
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
    }

    @PatchMapping("/{reference_id}/update")
    public ResponseEntity<?> updateCustomer(@PathVariable("reference_id") String userReferenceId,
        @Valid @RequestBody UserRequest userRequest) {
        log.info("update request received for user: {}", userReferenceId);
        return new ResponseEntity<>(userService.updateUser(userRequest), HttpStatus.CREATED);
    }

    @GetMapping("/details/{phone_number}/fetch")
    public ResponseEntity<?> fetchCustomerDetailsByPhoneNumber(@PathVariable("phone_number") String phoneNumber,
        @RequestHeader(value = "X-Source") String source) {
        log.info("fetch request received for user number: {} from source: {}", phoneNumber, source);
        return userService.findUserByPhoneNumberAndSource(phoneNumber, source)
            .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/details/email/fetch")
    public ResponseEntity<?> fetchCustomerDetailsByEmail(@RequestParam("email") String email,
        @RequestHeader(value = "X-Source") String source) {
        log.info("fetch request received for user email: {} from source: {}", email, source);
        return userService.findUserByEmailAndSource(email, source)
            .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
