package com.MyBackEnd.services;

import com.MyBackEnd.models.Project;
import com.MyBackEnd.models.ProjectRolesEnum;
import com.MyBackEnd.models.User;
import com.MyBackEnd.models.UserProjectRole;
import com.MyBackEnd.repository.ProjectRepository;
import com.MyBackEnd.repository.UserProjectRoleRepository;
import com.MyBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ContributorsService {
    private final UserProjectRoleRepository userProjectRoleRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    @Autowired
    public ContributorsService(UserProjectRoleRepository userProjectRoleRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.userProjectRoleRepository = userProjectRoleRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public List<UserProjectRole> getAllContributorsForProject(Integer projectId) {
        if (projectId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project ID cannot be null");
        }
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return userProjectRoleRepository.findByProject(project);
    }
        public UserProjectRole createContributorById(Integer projectId,int userId){
            if (projectId == null || userId == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project Id or User Id cannot be null");
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

            if (userProjectRoleRepository.findByUserAndProject(user, project) != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user already has a role in this project");
            }


            UserProjectRole userProjectRole = new UserProjectRole();
            userProjectRole.setRole(ProjectRolesEnum.Contributor);
            userProjectRole.setProject(project);
            userProjectRole.setUser(user);


            try {
                return userProjectRoleRepository.save(userProjectRole);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error creating contributor: " + e.getMessage());
            }
        }
    public UserProjectRole createContributorByEmail(Integer projectId,String email){
        if(projectId == null || email == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Project Id or Email cannot be null");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (userProjectRoleRepository.findByUserAndProject(user, project) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user already has a role in this project");
        }


        UserProjectRole userProjectRole = new UserProjectRole();
        userProjectRole.setRole(ProjectRolesEnum.Contributor);
        userProjectRole.setProject(project);
        userProjectRole.setUser(user);


        try {
            return userProjectRoleRepository.save(userProjectRole);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error creating contributor: " + e.getMessage());
        }
    }
    public UserProjectRole deleteContributorById(Integer projectId,int userId){
        if (projectId == null || userId == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project Id or User Id cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        UserProjectRole userProjectRole = userProjectRoleRepository.findByUserAndProject(user, project);
        if (userProjectRole == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have a role in this project");
        }

        // Prevent deletion of the owner
        if (userProjectRole.getRole().equals(ProjectRolesEnum.Owner)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot delete the Owner");
        }

        try {
            userProjectRoleRepository.delete(userProjectRole);
            return userProjectRole;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting contributor: " + e.getMessage());
        }
    }
    public UserProjectRole deleteContributorByEmail(Integer projectId, String email) {
        if (projectId == null || email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project Id or Email cannot be null");
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        UserProjectRole userProjectRole = userProjectRoleRepository.findByUserAndProject(user, project);
        if (userProjectRole == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have a role in this project");
        }

        // Prevent deletion of the owner
        if (userProjectRole.getRole().equals(ProjectRolesEnum.Owner)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot delete the Owner");
        }

        try {
            userProjectRoleRepository.delete(userProjectRole);
            return userProjectRole;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting contributor: " + e.getMessage());
        }
    }
}
