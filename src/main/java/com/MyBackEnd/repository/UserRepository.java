package com.MyBackEnd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.MyBackEnd.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Spring Data JPA will automatically implement this based on the method name
    boolean existsByEmail(String email);

    @Query(value = """
            SELECT *
            FROM users
            WHERE 
            email = :email
            LIMIT 1
            """, nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query(value = """
            SELECT *
            FROM users
            WHERE first_name = :firstName
            AND last_name = :lastName
            LIMIT 1
            """, nativeQuery = true)
    Optional<User> findByFirstAndLastName(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName);;
}
