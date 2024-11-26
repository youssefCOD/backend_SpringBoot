package com.MyBackEnd.repository;

import com.MyBackEnd.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Spring Data JPA will automatically implement this based on the method name
    boolean existsByEmail(String email);
    User findByEmail(String email);
}
