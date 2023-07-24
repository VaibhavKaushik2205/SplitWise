package com.project.splitwise.service.impl;

import com.project.splitwise.entity.Split;
import com.project.splitwise.repository.SplitRepository;
import com.project.splitwise.service.SplitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SplitServiceImpl implements SplitService {

    private final SplitRepository splitRepository;

    @Override
    public Split save(Split split) {
        return splitRepository.save(split);
    }
}
