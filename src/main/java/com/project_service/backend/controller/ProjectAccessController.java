package com.project_service.backend.controller;

import com.project_service.backend.dto.ProjectAccessDto;
import com.project_service.backend.service.ProjectAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/access")
public class ProjectAccessController {

    private final ProjectAccessService projectAccessService;

    @PostMapping("/join")
    public void joinToProject(@RequestBody ProjectAccessDto projectAccessDto) {
        projectAccessService.joinToProject(projectAccessDto);
    }

    @DeleteMapping("/leave/{projectId}/{participantId}")
    public void leaveFromProject(@PathVariable Long projectId,
                                 @PathVariable String participantId
    ) {
        projectAccessService.leaveFromProject(projectId, participantId);
    }
}
