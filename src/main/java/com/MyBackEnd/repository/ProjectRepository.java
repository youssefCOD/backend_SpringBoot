/*package com.MyBackEnd.repository;

import com.MyBackEnd.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Integer> {

    @Query(value = "SELECT user_id FROM Project WHERE creator_id = :user_id",nativeQuery = true)
    List<Project> getAllProjectsByUser(@Param("user_id") int user_id);
    @Query(value="SELECT user_id FROM Project WHERE project_id= :project_id AND creator_id = :user_id")
    Project getProjectByIdAndUser(@Param("project_id") int project_id,@Param("user_id") int user_id);

}*/

package com.MyBackEnd.repository;

import com.MyBackEnd.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    // Find all projects by creator
    List<Project> findByCreatorId(Integer creatorId);

    // Find specific project by id and creator
    @Query(value = "SELECT p.* FROM projects p " +
            "JOIN user_project_roles upr ON p.id = upr.project_id " +
            "WHERE p.id = :projectId AND upr.user_id = :userId",
            nativeQuery = true)
    Optional<Project> findProjectByIdAndUserId(@Param("projectId") Integer projectId, @Param("userId") Integer userId);

    // Additional useful methods you might want:
    boolean existsByIdAndCreatorId(Integer id, Integer creatorId);

    // Find projects by status and creator
    List<Project> findByStatusAndCreatorId(String status, Integer creatorId);

    // Find projects by title containing certain text (case-insensitive)
    List<Project> findByTitleContainingIgnoreCaseAndCreatorId(String title, Integer creatorId);

    // Count projects by creator
    long countByCreatorId(Integer creatorId);

    // Delete project by id and creator
    void deleteByIdAndCreatorId(Integer id, Integer creatorId);

    // Find projects ordered by creation date
    List<Project> findByCreatorIdOrderByCreatedAtDesc(Integer creatorId);
}
