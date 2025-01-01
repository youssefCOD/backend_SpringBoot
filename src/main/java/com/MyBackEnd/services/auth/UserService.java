package com.MyBackEnd.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.MyBackEnd.models.User;
import com.MyBackEnd.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Using Spring Data JPA's built-in method to find a user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null); // This method is automatically created by Spring Data
    } // JPA
    // Using Spring Data JPA's save method for inserting or updating users
    public User createUser(String first_name, String last_name, String email, String password, int color) {

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }

        User user = new User();
        user.setFirst_name(first_name);
        user.setLast_name(last_name);
        user.setEmail(email);
        user.setPassword(password);
        user.setColor(color);
        return userRepository.save(user); // Save the user to the database
    }
}
