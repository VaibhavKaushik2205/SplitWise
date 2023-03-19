package com.project.splitwise.repository;

import com.project.splitwise.entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByReferenceId(String userReferenceId);

    Optional<User> findByEmail(String userEmail);

    Optional<User> findByPhoneNumber(String userReferenceId);

}
