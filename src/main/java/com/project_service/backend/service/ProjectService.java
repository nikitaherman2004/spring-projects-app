package com.project_service.backend.service;

import com.project_service.backend.builder.SpecificationBuilder;
import com.project_service.backend.dto.*;
import com.project_service.backend.entity.Credentials;
import com.project_service.backend.entity.Project;
import com.project_service.backend.exception.ApplicationException;
import com.project_service.backend.mapper.ProjectMapper;
import com.project_service.backend.repository.ProjectRepository;
import com.project_service.backend.util.OwnershipRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectMapper projectMapper = ProjectMapper.INSTANCE;

    private final ProjectRepository projectRepository;

    private final SecurityAuthService securityAuthService;

    private final OwnershipRequestValidator requestValidator;

    public boolean existsProjectByKeyword(String keyword) {
        return projectRepository.existsByCredentialsKeyword(keyword);
    }

    public void saveProject(Project entity) {
        projectRepository.save(entity);
    }

    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }

    public ProjectResponseDto getProjectResponseDtoById(Long id) {
        Project project = getProjectById(id);

        return projectMapper.toDto(project);
    }

    public Project getProjectById(Long id) {
        Optional<Project> optional = projectRepository.findById(id);

        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ApplicationException(
                    String.format("Проект с идентификатором %s не найден", id),
                    HttpStatus.OK
            );
        }
    }

    public Project getProjectByCredentialsKeyword(String keyword) {
        Optional<Project> optional = projectRepository.findByCredentialsKeyword(keyword);

        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ApplicationException(
                    String.format("Проект с ключевым словом %s не найден", keyword),
                    HttpStatus.OK
            );
        }
    }

    public void updateProjectPassword(ProjectPasswordDto projectPasswordDto) {
        log.info(
                "Запрос на обновление пароля для проекта с идентификатором {}",
                projectPasswordDto.getProjectId()
        );
        requestValidator.validateRequestOwnership(projectPasswordDto.getUserId());

        Project project = getProjectById(projectPasswordDto.getProjectId());

        setNewPassword(project.getCredentials(), projectPasswordDto.getNewPassword());

        saveProject(project);
    }

    public Page<ProjectResponseDto> getProjectsByFilter(ProjectFilter projectFilter, Pageable pageable) {
        log.info(
                "Запрос на получение списка всех проектов с параметрами: название проекта - {}, " +
                        "статус приватности - {}",
                projectFilter.getName(), projectFilter.getIsPrivate()
        );

        Specification<Project> specification = new SpecificationBuilder()
                .andLike(projectFilter.getName(), "name")
                .andEqual(projectFilter.getIsPrivate(), "isPrivate")
                .build();

        Page<Project> projects = projectRepository.findAll(specification, pageable);

        return projects.map(projectMapper::toDto);
    }

    /**
     * This method updates the basic information about the project, including the name, description of the project,
     * as well as its privacy status.However, this method does not change the existing password in any way,
     * but only sets a new password or removes an existing one, depending on the privacy status
     */
    public void updateProject(ProjectUpdateDto projectUpdateDto) {
        log.info(
                "Запрос на изменение основной информации о проекте {} с идентификатором {}",
                projectUpdateDto.getName(), projectUpdateDto.getId()
        );

        Project project = getProjectById(projectUpdateDto.getId());
        mergeProject(project, projectUpdateDto);

        saveProject(project);
    }

    public void mergeProject(Project target, ProjectUpdateDto source) {
        target.setName(source.getName());
        target.setDescription(source.getDescription());

        boolean isPrivateStatusChanged = target.getIsPrivate() != source.getIsPrivate();

        if (isPrivateStatusChanged) {
            target.setIsPrivate(source.getIsPrivate());
            Credentials credentials = target.getCredentials();

            setPasswordOptionally(target, source.getPassword(), credentials);
        }
    }

    /**
     * This method creates a new project, provided that the project with the specified "keyword"
     * field does not exist. Also, depending on the privacy status, the project is given a new password or not
     */
    public void createProject(ProjectCreateDto createDto) {
        log.info(
                "Запрос на создание нового проекта с названием {}, идентификатор пользователя - {}",
                createDto.getName(), createDto.getUserId()
        );
        requestValidator.validateRequestOwnership(createDto.getUserId());

        Project entity = projectMapper.toEntity(createDto);

        Credentials credentials = entity.getCredentials();

        if (existsProjectByKeyword(credentials.getKeyword())) {
            throw new ApplicationException(
                    String.format("Проект с названием %s уже существует", credentials.getKeyword()),
                    HttpStatus.BAD_REQUEST
            );
        }
        setPasswordOptionally(entity, credentials.getPassword(), credentials);

        saveProject(entity);
    }

    private void setPasswordOptionally(Project entity, String password, Credentials credentials) {
        if (entity.getIsPrivate()) {
            setNewPassword(credentials, password);
        } else {
            credentials.setPassword(null);
        }
    }

    private void setNewPassword(Credentials credentials, String password) {
        String encodedPassword = securityAuthService.encodePassword(password);

        credentials.setPassword(encodedPassword);
    }
}
