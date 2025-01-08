package com.project_service.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectPasswordDto {

    private String userId;

    private Long projectId;

    private String newPassword;
}
