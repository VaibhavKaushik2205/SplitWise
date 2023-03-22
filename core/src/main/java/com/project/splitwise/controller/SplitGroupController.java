package com.project.splitwise.controller;

import com.project.splitwise.contract.request.SplitGroupRequest;
import com.project.splitwise.service.SplitGroupService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("split-groups")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SplitGroupController {

    private final SplitGroupService splitGroupService;

    @PostMapping
    public ResponseEntity<?> createGroup(@Valid @RequestBody SplitGroupRequest groupRequest) {
        log.info("create split group request received with name: {}", groupRequest.getNameOfGroup());
        return new ResponseEntity<>(splitGroupService.createGroup(groupRequest), HttpStatus.CREATED);
    }

    @PatchMapping("/{group_reference_id}/update")
    public ResponseEntity<?> createGroup(@PathVariable("group_reference_id") String groupReferenceId,
        @Valid @RequestBody SplitGroupRequest groupRequest) {
        log.info("update split group request received with id: {}", groupReferenceId);
        return new ResponseEntity<>(splitGroupService.updateGroup(groupReferenceId, groupRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{group_reference_id}")
    public ResponseEntity<?> getSplitGroupDetails(@PathVariable("group_reference_id") String groupReferenceId) {
        log.info("fetch split group request received for group: {}", groupReferenceId);
        return splitGroupService.fetchGroupByReferenceId(groupReferenceId)
            .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
