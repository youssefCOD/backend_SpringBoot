package com.MyBackEnd.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import com.MyBackEnd.controllers.DTO.UserRequest;
import com.MyBackEnd.controllers.DTO.UserResponse;
import com.MyBackEnd.models.UserProjectRole;
import com.MyBackEnd.repository.UserRepository;
import com.MyBackEnd.services.ContributorsService;

@RestController
@RequestMapping("/api/contributors")
public class ContributorsController {
    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);
    private final UserRepository userRepository;

    @Autowired
    ContributorsController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private ContributorsService contributorsService;

    // Get all the contributors
    @GetMapping("/project/{project_id}")
    public ResponseEntity<List<UserProjectRole>> getAllContributors(@PathVariable("project_id") Integer projectId) {
        List<UserProjectRole> contributors = contributorsService.getAllContributorsForProject(projectId);
        return ResponseEntity.ok(contributors);
    }

    @PostMapping("/project/{project_id}/user/{user_id}")
    public ResponseEntity<UserProjectRole> createContributor(@PathVariable("project_id") Integer projectId,
            @PathVariable("user_id") int userId) {
        try {
            UserProjectRole contributor = contributorsService.createContributorById(projectId, userId);
            return ResponseEntity.ok(contributor);
        } catch (Exception e) {
            log.error("Error creating contributor: ", e);
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/project/{project_id}")
    public ResponseEntity<UserProjectRole> createContributor(@PathVariable("project_id") Integer projectId,
            @RequestParam("email") String email) {
        try {
            UserProjectRole contributor = contributorsService.createContributorByEmail(projectId, email);
            return ResponseEntity.ok(contributor);
        } catch (Exception e) {
            log.error("Error creating contributor: ", e);
            return ResponseEntity.status(500).build();
        }

    }

    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getMember(@RequestBody UserRequest request) {
        try {
            UserResponse userResponse;
            if (request.getUsername() != null && !request.getUsername().trim().isEmpty()) {
                final String[] split = request.getUsername().split(" ");
                if (split.length != 2) {
                    return ResponseEntity.badRequest().build();
                }
                final String firstName = split[0];
                final String lastName = split[1];
                log.info("Username: {}", request.getUsername());

                userResponse = new UserResponse(userRepository.findByFirstAndLastName(firstName, lastName)
                        .orElseThrow(() -> new NoSuchElementException("User not found")));
                log.info("UserResponse: {}", userResponse);
            } else if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                log.info("Email: {}", request.getEmail());
                userResponse = new UserResponse(userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new NoSuchElementException("User not found")));
                log.info("UserResponse: {}", userResponse);

            } else {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userResponse);

        } catch (NoSuchElementException e) {
            log.error("User not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error processing request: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // TODO : fix this
    @DeleteMapping("/project/{project_id}/user/{user_id}")
    public ResponseEntity<UserProjectRole> deleteContributor(@PathVariable("project_id") Integer projectId,
            @PathVariable("user_id") int userId) {
        try {
            UserProjectRole contributor = contributorsService.deleteContributorById(projectId, userId);
            return ResponseEntity.ok(contributor);
        } catch (Exception e) {
            log.error("Error creating contributor: ", e);
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/project/{project_id}")
    public ResponseEntity<UserProjectRole> deleteContributor(@PathVariable("project_id") Integer projectId,
            @RequestParam("email") String email) {
        try {
            UserProjectRole contributor = contributorsService.deleteContributorByEmail(projectId, email);
            return ResponseEntity.ok(contributor);
        } catch (Exception e) {
            log.error("Error creating contributor: ", e);
            return ResponseEntity.status(500).build();
        }
    }
}
