package com.project_service.backend.controller;

import com.project_service.backend.dto.*;
import com.project_service.backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    private ProjectResponseDto getProjectById(@PathVariable Long id) {
        return projectService.getProjectResponseDtoById(id);
    }

    @GetMapping("/all")
    private Page<ProjectResponseDto> getProjectsByFilter(
            @ModelAttribute ProjectFilter projectFilter,
            Pageable pageable
    ) {
        return projectService.getProjectsByFilter(projectFilter, pageable);
    }

    @PostMapping
    private void createProject(@RequestBody ProjectCreateDto createDto) {
        projectService.createProject(createDto);
    }

    @PutMapping
    private void updateProject(@RequestBody ProjectUpdateDto updateDto) {
        projectService.updateProject(updateDto);
    }

    @PutMapping("/password")
    private void updateProjectPassword(@RequestBody ProjectPasswordDto projectPasswordDto) {
        projectService.updateProjectPassword(projectPasswordDto);
    }

    @DeleteMapping("/{id}")
    private void deleteProjectById(@PathVariable Long id) {
        projectService.deleteProjectById(id);
    }
}
