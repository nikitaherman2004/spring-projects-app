package com.project_service.backend.repository;

import com.project_service.backend.entity.ProjectParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectParticipantRepository extends JpaRepository<ProjectParticipant, Long> {

    Boolean existsByParticipantIdAndProjectId(String participantId, Long projectId);

    Optional<ProjectParticipant> findByParticipantIdAndProjectId(String participantId, Long project_id);
}
