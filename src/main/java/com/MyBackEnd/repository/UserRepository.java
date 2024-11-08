package com.MyBackEnd.repository;

import com.MyBackEnd.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
    @Query(value = "SELECT * FROM Users WHERE email = :email",nativeQuery = true)
    User getUserByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO users (first_name , last_name , email , password) VALUE(:first_name , :last_name , :email , :password)",nativeQuery = true)
    int registerUser(@Param("first_name") String fist_name,
                     @Param("last_name") String last_name,
                     @Param("email") String email,
                     @Param("password") String password);
}
