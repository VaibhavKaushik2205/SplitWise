package com.project.splitwise.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReadinessCheckController {

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return new ResponseEntity<>("PONG", HttpStatus.OK);
    }
}
