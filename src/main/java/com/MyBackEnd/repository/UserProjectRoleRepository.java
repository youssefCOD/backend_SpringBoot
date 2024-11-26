package com.MyBackEnd.repository;

import com.MyBackEnd.models.Project;
import com.MyBackEnd.models.User;
import com.MyBackEnd.models.UserProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserProjectRoleRepository extends JpaRepository<UserProjectRole,Integer> {
    List<UserProjectRole> findByProject(Project project);

    UserProjectRole findByUser(User user);
    UserProjectRole findByUserAndProject(User user, Project project);
}
