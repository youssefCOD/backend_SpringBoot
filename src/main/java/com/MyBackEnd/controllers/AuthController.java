package com.MyBackEnd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MyBackEnd.controllers.DTO.RegistrationResponse;
import com.MyBackEnd.dto.requests.LoginRequest;
import com.MyBackEnd.dto.responses.AuthResponse;
import com.MyBackEnd.models.User;
import com.MyBackEnd.services.JwtTokenServices;
import com.MyBackEnd.services.auth.MyCustomUserDetails;
import com.MyBackEnd.services.auth.MyCustomUserDetailsService;
import com.MyBackEnd.services.auth.UserService;

@RestController
@RequestMapping("api/v1/auth")
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
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // set user object :
        final MyCustomUserDetails userDetails =
                (MyCustomUserDetails) myCustomUserDetailsService.loadUserByUsername(loginRequest.getEmail());

        // set security context:
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //generate token:
        String token = jwtTokenServices.generateToken(userDetails);



        AuthResponse response = new AuthResponse(token , userDetails);

        return new ResponseEntity(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestParam("first_name") String firstName,
                                           @RequestParam("last_name") String lastName,
                                           @RequestParam("email") String email,
                                           @RequestParam("password") String password,
                                            @RequestParam("color") int color) {
        // Hash password
        String hashed_password = passwordEncoder.encode(password);

        // Register the user using JPA (save the user)
        User createdUser = userService.createUser(firstName, lastName, email, hashed_password, color);

        // Check if user was successfully created
        if (createdUser == null) {
            RegistrationResponse errorResponse = new RegistrationResponse("Something went wrong", "failure");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        final MyCustomUserDetails userDetails
                = (MyCustomUserDetails) myCustomUserDetailsService.loadUserByUsername(email);

        // set security context:
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //generate token:
        String token = jwtTokenServices.generateToken(userDetails);

        AuthResponse response = new AuthResponse(token, userDetails);

        // so that we can rature a json(tabrkalah makontch 3araf tsa7bli radi traj3lihom l7ammas)
        //* */ Old code
        //* */ RegistrationResponse successResponse = new RegistrationResponse("Registration Successful.", "success");
        //* */ Return success message with HTTP status code
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

}
