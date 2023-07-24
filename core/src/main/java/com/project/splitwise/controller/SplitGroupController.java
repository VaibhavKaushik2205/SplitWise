package com.project.splitwise.controller;

import com.project.splitwise.contract.request.SplitGroupRequest;
import com.project.splitwise.service.SplitGroupService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/split-groups")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SplitGroupController {

    private final SplitGroupService splitGroupService;

    @PostMapping("")
    public ResponseEntity<?> createGroup(@Valid @RequestBody SplitGroupRequest groupRequest) {
        log.info("create split group request received with name: {}", groupRequest.getNameOfGroup());
        return new ResponseEntity<>(splitGroupService.createGroup(groupRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{group_reference_id}/update")
    public ResponseEntity<?> updateGroup(@PathVariable("group_reference_id") String groupReferenceId,
        @Valid @RequestBody SplitGroupRequest groupRequest) {
        log.info("update split group request received with id: {}", groupReferenceId);
        return new ResponseEntity<>(splitGroupService.updateGroup(groupReferenceId, groupRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{group_reference_id}")
    public ResponseEntity<?> getSplitGroupDetails(@PathVariable("group_reference_id") String groupReferenceId) {
        log.info("fetch split group request received for group: {}", groupReferenceId);
        return splitGroupService.findGroupByReferenceId(groupReferenceId)
            .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
