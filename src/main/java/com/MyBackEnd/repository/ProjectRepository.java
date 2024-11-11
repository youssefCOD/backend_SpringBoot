package com.MyBackEnd.repository;

import com.MyBackEnd.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {

    @Query(value = "SELECT * FROM Project WHERE creator_id = :user_id",nativeQuery = true)
    List<Project> getAllProjectsByUser(@Param("user_id") int user_id);
    @Query(value="SELECT * FROM Project WHERE project_id= :project_id AND creator_id = :user_id")
    Project getProjectByIdAndUser(@Param("project_id") int project_id,@Param("user_id") int user_id);

}
