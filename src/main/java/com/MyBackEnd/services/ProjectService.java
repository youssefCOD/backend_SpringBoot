package com.MyBackEnd.services;

import com.MyBackEnd.models.Project;
import com.MyBackEnd.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

//    find all projects by user
    public List<Project> getAllProjectsForUser(int userId){
        return projectRepository.getAllProjectsByUser(userId);
    }
//    find a project by project id and user id
    public Project getProjectByIdAndUser(int projectId, int userId){
        return projectRepository.getProjectByIdAndUser(projectId,userId);
    }
//    create a new project
    public Project createProject(Project project){
        return projectRepository.save(project);
    }
//    update a project
    public Project updateProject(int projectId,Project projectDetails){
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        existingProject.setName(projectDetails.getName());
        existingProject.setDescription(projectDetails.getDescription());
        existingProject.setCreator_id(projectDetails.getCreator_id());
        existingProject.setStart_date(projectDetails.getStart_date());
        existingProject.setEnd_date(projectDetails.getEnd_date());
        existingProject.setUpdated_at(LocalDateTime.now());
        existingProject.setProgress(projectDetails.getProgress());
        return projectRepository.save(existingProject);
    }
//    delete a project
    public void deleteProject(int projectId){
        projectRepository.deleteById(projectId);
    }
}
