package br.com.hub.connect.application.project.project.service;

import br.com.hub.connect.domain.project.model.Project;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import br.com.hub.connect.application.project.project.dto.CreateProjectDTO;
import br.com.hub.connect.application.project.project.dto.ProjectResponseDTO;
import br.com.hub.connect.application.project.project.dto.UpdateProjectDTO;
import br.com.hub.connect.application.user.dto.UserResponseDTO;
import br.com.hub.connect.domain.exception.ProjectNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.hub.connect.domain.user.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProjectService {

  public List<ProjectResponseDTO> findAll(int page, int size) {
    return Project.findAllActive(page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public ProjectResponseDTO findById(@NotNull Long id) {
    Project project = Project.findActiveById(id)
        .orElseThrow(() -> new ProjectNotFoundException(id));

    return toResponseDTO(project);
  }

  @Transactional
  public ProjectResponseDTO create(@Valid CreateProjectDTO dto) {

    Project project = new Project();
    project.name = dto.name();
    project.description = dto.description();
    project.technologies = Set.of(dto.technologies());
    project.repositoryUrl = dto.repositoryUrl();
    project.imageUrl = dto.imageUrl();
    project.author = User.findById(dto.authorId());

    project.persistAndFlush();
    return toResponseDTO(project);
  }

  @Transactional
  public ProjectResponseDTO update(@NotNull @Positive Long id, @Valid UpdateProjectDTO dto) {
    Project project = Project.findActiveById(id)
        .orElseThrow(() -> new ProjectNotFoundException(id));

    if (dto.name() != null) {
      project.name = dto.name();
    }
    if (dto.description() != null) {
      project.description = dto.description();
    }
    if (dto.technologies() != null) {
      project.technologies = Set.of(dto.technologies());
    }
    if (dto.repositoryUrl() != null) {
      project.repositoryUrl = dto.repositoryUrl();
    }
    if (dto.imageUrl() != null) {
      project.imageUrl = dto.imageUrl();
    }
    if (dto.authorId() != null) {
      project.author = User.findById(dto.authorId());
    }
    if (dto.countViews() != null) {
      project.countViews = dto.countViews();
    }

    project.persist();
    return toResponseDTO(project);
  }

  @Transactional
  public void delete(@NotNull @Positive Long id) {
    Project project = Project.findActiveById(id)
        .orElseThrow(() -> new ProjectNotFoundException(id));
    project.isActive = false;
    project.persist();
  }

  public long count() {
    return Project.countActive();
  }

  private ProjectResponseDTO toResponseDTO(Project project) {
    return new ProjectResponseDTO(
        project.id,
        project.name,
        project.description,
        project.repositoryUrl,
        project.imageUrl,
        project.technologies,
        project.countViews,
        project.createdAt,
        toUserResponse(project.author));
  }

  private UserResponseDTO toUserResponse(User user) {
    return new UserResponseDTO(
        user.id,
        user.name,
        user.email,
        user.role,
        user.enrollmentId,
        user.CPF,
        user.phone,
        user.xp,
        user.level,
        user.avatarUrl,
        user.isActive,
        user.createdAt);
  }
}
