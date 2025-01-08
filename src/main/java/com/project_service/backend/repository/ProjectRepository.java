package com.project_service.backend.repository;

import com.project_service.backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    Boolean existsByCredentialsKeyword(String keyword);

    Optional<Project> findByCredentialsKeyword(String keyword);
}
