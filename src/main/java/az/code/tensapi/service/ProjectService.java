package az.code.tensapi.service;

import az.code.tensapi.dto.request.CategoryRequest;
import az.code.tensapi.dto.request.ProjectRequest;
import az.code.tensapi.dto.response.CategoryResponse;
import az.code.tensapi.dto.response.ProjectResponse;
import az.code.tensapi.entity.Category;
import az.code.tensapi.entity.Project;
import az.code.tensapi.exception.CategoryNotFoundException;
import az.code.tensapi.exception.ErrorCodes;
import az.code.tensapi.repository.CategoryRepository;
import az.code.tensapi.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    public List<ProjectResponse> findAll() {
        return projectRepository
                .findAll()
                .stream()
                .map(project -> modelMapper.map(project, ProjectResponse.class))
                .collect(Collectors.toList());
    }

    public ProjectResponse findById(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new CategoryNotFoundException(ErrorCodes.PROJECT_NOT_FOUND));

        return modelMapper.map(project, ProjectResponse.class);
    }

    public ProjectResponse save(ProjectRequest request) {
        Project project = modelMapper.map(request, Project.class);

        return modelMapper.map(projectRepository.save(project), ProjectResponse.class);
    }


    public ProjectResponse updateCategory(Long projectId, ProjectRequest request) {
        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new CategoryNotFoundException(ErrorCodes.PROJECT_NOT_FOUND));

        if (request.getName() != null) {
            project.setName(request.getName());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getStartDate() != null) {
            project.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            project.setEndDate(request.getEndDate());
        }

        Project updatedProject = projectRepository.save(project);

        return modelMapper.map(updatedProject, ProjectResponse.class);
    }

    public void delete(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new CategoryNotFoundException(ErrorCodes.PROJECT_NOT_FOUND));

        projectRepository.delete(project);
    }
}
