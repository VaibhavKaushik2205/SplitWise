package com.project.splitwise.repository;

import com.project.splitwise.entity.Split;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SplitRepository extends JpaRepository<Split, Long> {

}
