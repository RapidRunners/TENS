package az.code.tensapi.service.impl;

import az.code.tensapi.dto.request.TaskRequest;
import az.code.tensapi.dto.response.TaskResponse;
import az.code.tensapi.entity.Category;
import az.code.tensapi.entity.Project;
import az.code.tensapi.entity.Task;
import az.code.tensapi.entity.User;
import az.code.tensapi.exception.*;
import az.code.tensapi.repository.CategoryRepository;
import az.code.tensapi.repository.ProjectRepository;
import az.code.tensapi.repository.TaskRepository;
import az.code.tensapi.repository.UserRepository;
import az.code.tensapi.service.AiService;
import az.code.tensapi.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AiService aiService;

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
        Map<String, String> messages = getStringStringMap(request.getName() + request.getDescription());
        String suggestedSummary = aiService.summarize(messages);
        task.setSummary(suggestedSummary);


        if (request.getCategoryId() != null) {
            Optional<Category> optionalCategory = categoryRepository.findById(request.getCategoryId());
            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();
                task.setCategory(category);
            } else {
                throw new CategoryNotFoundException(ErrorCodes.CATEGORY_NOT_FOUND);
            }
        } else {
            String prompt = "Suggest category for task: " + task.getName();
            String suggestedCategory = aiService.chat("user", prompt);

            Category category = new Category();
            category.setName(suggestedCategory);

            Category savedCategory = categoryRepository.save(category);

            task.setCategory(savedCategory);
        }

        if (request.getProjectId() != null) {
            Optional<Project> optionalProject = projectRepository.findById(request.getProjectId());
            if (optionalProject.isPresent()) {
                Project project = optionalProject.get();
                task.setProject(project);
            } else {
                throw new ProjectNotFoundException(ErrorCodes.PROJECT_NOT_FOUND);
            }
        }

        return modelMapper.map(taskRepository.save(task), TaskResponse.class);
    }

    private static Map<String, String> getStringStringMap(String userPrompt ) {
        Map<String, String> messages = new HashMap<>();
        String systemPrompt = "Use the following step-by-step instructions to respond to user inputs.\n" +
                " The user will provide you with text in triple quotes. Summarize this text in one sentence with a prefix that says \"Summary: \".\n";
        messages.put("user", userPrompt);
        messages.put("system", systemPrompt);
        return messages;
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

    public void addUserToTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(404, "Task not found with ID: " + taskId));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(404, "User not found with ID: " + userId));

        task.getAccounts().add(user);
        taskRepository.save(task);
    }

    public void removeUserFromTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(404, "Task not found with ID: " + taskId));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(404, "User not found with ID: " + userId));

        task.getAccounts().remove(user);
        taskRepository.save(task);
    }
}
