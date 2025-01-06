package com.project_service.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectAccessDto {

    private String userId;

    private String keyword;

    private String password;
}
