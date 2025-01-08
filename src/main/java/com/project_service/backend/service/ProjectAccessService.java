package com.project_service.backend.service;

import com.project_service.backend.dto.ProjectAccessDto;
import com.project_service.backend.entity.Credentials;
import com.project_service.backend.entity.Project;
import com.project_service.backend.entity.ProjectParticipant;
import com.project_service.backend.exception.ApplicationException;
import com.project_service.backend.repository.ProjectParticipantRepository;
import com.project_service.backend.util.OwnershipRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectAccessService {

    private final ProjectService projectService;

    private final SecurityAuthService securityAuthService;

    private final OwnershipRequestValidator requestValidator;

    private final ProjectParticipantRepository projectParticipantRepository;

    public void saveProjectParticipant(ProjectParticipant projectParticipant) {
        projectParticipantRepository.save(projectParticipant);
    }

    public void deleteProjectParticipant(ProjectParticipant projectParticipant) {
        projectParticipantRepository.delete(projectParticipant);
    }

    public Boolean existsByParticipantIdAndProjectId(String participantId, Long projectId) {
        return projectParticipantRepository.existsByParticipantIdAndProjectId(participantId, projectId);
    }

    public ProjectParticipant findByParticipantIdAndProjectId(String participantId, Long projectId) {
        Optional<ProjectParticipant> optional = projectParticipantRepository.findByParticipantIdAndProjectId(
                participantId, projectId
        );

        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ApplicationException(
                    String.format(
                            "Не удалось найти участника проекта с идентификаторами пользователя %s и проекта %s",
                            participantId,
                            projectId),
                    HttpStatus.OK
            );
        }
    }

    /**
     * This method finds the project by its keyword and verifies the password entered by the user.
     * If the passwords match, the user gets access to the requested project by its keyword and becomes a new participant
     */
    public void joinToProject(ProjectAccessDto projectAccessDto) {
        log.info(
                "Запрос на присоединения нового пользователя к проекту с ключевым словом {}. " +
                        "Идентификатор инициатора - {}",
                projectAccessDto.getKeyword(), projectAccessDto.getUserId()
        );
        requestValidator.validateRequestOwnership(projectAccessDto.getUserId());

        Project project = projectService.getProjectByCredentialsKeyword(projectAccessDto.getKeyword());

        if (existsByParticipantIdAndProjectId(projectAccessDto.getUserId(), project.getId())) {
            throw new ApplicationException(
                    String.format("Вы уже являетесь участником проекта %s", projectAccessDto.getKeyword()),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (project.getIsPrivate()) {
            checkPasswordOrThrowException(project, projectAccessDto);
        }

        createNewProjectParticipantAndSave(project, projectAccessDto.getUserId());
    }

    /**
     * This method searches for a project participant by their ID and project ID.
     * If there is, it removes the user from the list, completely restricting access to the project
     */
    public void leaveFromProject(Long projectId, String participantId) {
        log.info(
                "Запрос на прекращение участия в проекте с идентификатором проекта {}. Идентификатор инициатора {}",
                projectId, participantId
        );
        requestValidator.validateRequestOwnership(participantId);

        ProjectParticipant projectParticipant = findByParticipantIdAndProjectId(participantId, projectId);

        deleteProjectParticipant(projectParticipant);
    }

    private void checkPasswordOrThrowException(Project project, ProjectAccessDto projectAccessDto) {
        Credentials credentials = project.getCredentials();

        if (!securityAuthService.matchesPassword(projectAccessDto.getPassword(), credentials.getPassword())) {
            throw new ApplicationException(
                    String.format(
                            "Не удалось присоединиться к проекту. Вы ввели неправильный пароль для " +
                                    "запрашиваемого проекта с ключевым словом %s",
                            projectAccessDto.getKeyword()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void createNewProjectParticipantAndSave(Project project, String participantId) {
        ProjectParticipant projectParticipant = ProjectParticipant.builder()
                .project(project)
                .participantId(participantId)
                .build();

        saveProjectParticipant(projectParticipant);
    }
}
