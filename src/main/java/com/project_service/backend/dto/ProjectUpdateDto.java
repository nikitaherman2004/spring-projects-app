package com.project_service.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectUpdateDto {

    private Long id;

    private String name;

    private String password;

    private Boolean isPrivate;

    private String description;
}
