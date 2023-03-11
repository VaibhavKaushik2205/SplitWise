package com.project.splitwise.repository;

import com.project.splitwise.entity.SplitGroup;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends CrudRepository<SplitGroup, Integer> {

    Optional<SplitGroup> findById(Long id);

//    List<SplitGroup> findByMembers(Long )
}
