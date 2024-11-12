package com.MyBackEnd.controllers;

import com.MyBackEnd.dto.requests.LoginRequest;
import com.MyBackEnd.dto.responses.AuthResponse;
import com.MyBackEnd.models.User;
import com.MyBackEnd.services.JwtTokenServices;
import com.MyBackEnd.services.auth.MyCustomUserDetails;
import com.MyBackEnd.services.auth.MyCustomUserDetailsService;
import com.MyBackEnd.services.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private MyCustomUserDetailsService myCustomUserDetailsService;
    @Autowired
    private JwtTokenServices jwtTokenServices;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){

        //set authentication:
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // set user object :
        MyCustomUserDetails userDetails =
                (MyCustomUserDetails) myCustomUserDetailsService.loadUserByUsername(loginRequest.getEmail());

        //set security context:
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //generate token:
        String token = jwtTokenServices.generateToken(userDetails);



        AuthResponse response = new AuthResponse(token , userDetails);

        return new ResponseEntity(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam("first_name") String firstname,
                                           @RequestParam("last_name") String lastName,
                                           @RequestParam("email") String email,
                                           @RequestParam("password") String password) {
        // Hash password
        String hashed_password = passwordEncoder.encode(password);

        // Register the user using JPA (save the user)
        User createdUser = userService.registerUser(firstname, lastName, email, hashed_password);

        // Check if user was successfully created
        if (createdUser == null) {
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }

        // Return success message with HTTP status code
        return new ResponseEntity<>("Registration Successful.", HttpStatus.CREATED);
    }

}
