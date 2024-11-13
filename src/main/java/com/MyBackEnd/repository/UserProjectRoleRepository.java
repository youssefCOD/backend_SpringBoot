package com.MyBackEnd.repository;

import com.MyBackEnd.models.Project;
import com.MyBackEnd.models.UserProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProjectRoleRepository extends JpaRepository<UserProjectRole,Integer> {
    List<UserProjectRole> findByProject(Project project);
}
