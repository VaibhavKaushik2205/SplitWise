package com.project.splitwise.repository;

import com.project.splitwise.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

//    Optional<User> findByContact(String contact);
}
