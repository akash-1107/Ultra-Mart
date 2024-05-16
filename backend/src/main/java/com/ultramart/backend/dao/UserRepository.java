package com.ultramart.backend.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ultramart.backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findFirstByEmail(String email);
    
}
