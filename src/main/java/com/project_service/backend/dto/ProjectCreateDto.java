package com.project_service.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectCreateDto {

    private String name;

    private String userId;

    private Boolean isPrivate;

    private String description;

    private CredentialsDto credentials;
}