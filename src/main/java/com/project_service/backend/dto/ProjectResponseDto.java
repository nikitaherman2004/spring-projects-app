package com.project_service.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectResponseDto {

    private Long id;

    private String name;

    private String description;
}
