package com.project_service.backend.mapper;

import com.project_service.backend.dto.ProjectCreateDto;
import com.project_service.backend.dto.ProjectResponseDto;
import com.project_service.backend.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mapping(target = "createdBy", source = "userId")
    Project toEntity(ProjectCreateDto createDto);

    ProjectResponseDto toDto(Project project);
}
