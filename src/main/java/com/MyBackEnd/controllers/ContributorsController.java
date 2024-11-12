package com.MyBackEnd.controllers;

import com.MyBackEnd.models.UserProjectRole;
import com.MyBackEnd.services.ContributorsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contributors")
public class ContributorsController {
    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);
    @Autowired
    private ContributorsService contributorsService;

//    Get all the contributors
    @GetMapping("/project/{project_id}")
    public ResponseEntity<List<UserProjectRole>> getAllContributors(@PathVariable("project_id")Integer projectId) {
        List<UserProjectRole> contributors = contributorsService.getAllContributorsForProject(projectId);
        return ResponseEntity.ok(contributors);
    }
    @PostMapping("/project/{project_id}/user/{user_id}")
    public ResponseEntity<UserProjectRole> createContributor(@PathVariable("project_id")Integer projectId, @PathVariable("user_id") int userId){
        try {
            UserProjectRole contributor = contributorsService.createContributor(projectId,userId);
            return ResponseEntity.ok(contributor);
        } catch (Exception e) {
            log.error("Error creating contributor: ", e);
            return ResponseEntity.status(500).build();
        }
    }
}
