package com.MyBackEnd.controllers;

import com.MyBackEnd.models.UserProjectRole;
import com.MyBackEnd.services.ContributorsService;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contributors")
public class ContributorsController {
    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);
    @Autowired
    private ContributorsService contributorsService;

//    Get all the contributors
    @GetMapping("/project/{project_id}")
    public ResponseEntity<List<UserProjectRole>> getAllContributors(@PathParam("project_id")Integer projectId) {
        List<UserProjectRole> contributors = contributorsService.getAllContributorsForProject(projectId);
        return ResponseEntity.ok(contributors);
    }
    @PostMapping("/project/{projects_id}/user/u{user_id}")
    public ResponseEntity<UserProjectRole> createContributor(@PathParam("project_id")Integer projectId,@PathParam("user_id") int userId){
        try {
            UserProjectRole contributor = contributorsService.createContributor(projectId,userId);
            return ResponseEntity.ok(contributor);
        } catch (Exception e) {
            log.error("Error creating project: ", e);
            return ResponseEntity.status(500).build();
        }
    }
}
