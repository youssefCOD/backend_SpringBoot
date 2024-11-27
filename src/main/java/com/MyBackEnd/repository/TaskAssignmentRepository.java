package com.MyBackEnd.repository;

import com.MyBackEnd.models.Task;
import com.MyBackEnd.models.TaskAssignment;
import com.MyBackEnd.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment,Integer> {
    List<TaskAssignment> findByTask(Task task);

    TaskAssignment findByUserAndTask(User user, Task task);
}
