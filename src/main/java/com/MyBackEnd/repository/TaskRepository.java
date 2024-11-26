package com.MyBackEnd.repository;

import com.MyBackEnd.models.Project;
import com.MyBackEnd.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Integer> {
    List<Task> findByProject(Project project);
}
