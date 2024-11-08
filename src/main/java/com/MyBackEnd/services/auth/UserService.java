package com.MyBackEnd.services.auth;

import com.MyBackEnd.models.User;
import com.MyBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    public int registerUser(String first_name, String last_name , String email , String password){
        return userRepository.registerUser(first_name, last_name, email, password);
    }

}
