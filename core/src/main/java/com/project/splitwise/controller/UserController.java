package com.project.splitwise.controller;

import com.project.splitwise.contract.request.UserRequest;
import com.project.splitwise.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("createCustomer request received for customer: {}", userRequest.getPhoneNumber());
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{reference_id}/update")
    public ResponseEntity<?> updateCustomer(@PathVariable("reference_id") String userReferenceId,
        @Valid @RequestBody UserRequest userRequest) {
        log.info("update request received for user: {}", userReferenceId);
        return new ResponseEntity<>(userService.updateUser(userReferenceId, userRequest), HttpStatus.CREATED);
    }

    @GetMapping("/details/phone_number")
    public ResponseEntity<?> getCustomerDetailsByPhoneNumber(@RequestParam("phone_number") String phoneNumber,
        @RequestHeader(value = "X-Source") String source) {
        log.info("[GET] request received for user number: {} from source: {}", phoneNumber, source);
        return userService.findUserByPhoneNumberAndSource(phoneNumber, source)
            .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/details/email")
    public ResponseEntity<?> getCustomerDetailsByEmail(@RequestParam("email") String email,
        @RequestHeader(value = "X-Source") String source) {
        log.info("[GET] request received for user email: {} from source: {}", email, source);
        return userService.findUserByEmailAndSource(email, source)
            .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
