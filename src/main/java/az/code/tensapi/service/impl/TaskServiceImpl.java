package az.code.tensapi.service.impl;

import az.code.tensapi.dto.request.TaskRequest;
import az.code.tensapi.dto.response.TaskResponse;
import az.code.tensapi.entity.Category;
import az.code.tensapi.entity.Project;
import az.code.tensapi.entity.Task;
import az.code.tensapi.exception.TaskNotFoundException;
import az.code.tensapi.repository.CategoryRepository;
import az.code.tensapi.repository.ProjectRepository;
import az.code.tensapi.repository.TaskRepository;
import az.code.tensapi.repository.UserRepository;
import az.code.tensapi.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<TaskResponse> getAll() {
        return taskRepository
                .findAll()
                .stream()
                .map(task -> modelMapper.map(task, TaskResponse.class))
                .toList();

    }

    @Override
    public TaskResponse create(TaskRequest request) {
        Task task = modelMapper.map(request, Task.class);

        Project project = projectRepository.findById(request.getProjectId()).orElseThrow();
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow();
        task.setCategory(category);
        task.setProject(project);

        return modelMapper.map(taskRepository.save(task), TaskResponse.class);

    }

    @Override
    public TaskResponse update(Long id, TaskRequest request) {
        taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException(404, "Task not found"));


        Task task = modelMapper.map(request, Task.class);
        task.setId(id);
        Project project = projectRepository.findById(request.getProjectId()).orElseThrow();
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow();
        task.setCategory(category);
        task.setProject(project);

        return modelMapper.map(taskRepository.save(task), TaskResponse.class);

    }

    @Override
    public void delete(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException(404, "Task not found"));

        taskRepository.delete(task);

    }

}
