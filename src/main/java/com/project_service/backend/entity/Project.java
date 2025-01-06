package com.project_service.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(name = "private")
    private Boolean isPrivate;

    private String createdBy;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "credentials_id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Credentials credentials;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectParticipant> participants;
}

