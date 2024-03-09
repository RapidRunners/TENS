package az.code.tensapi.controller;

import az.code.tensapi.dto.request.TaskRequest;
import az.code.tensapi.dto.response.TaskResponse;
import az.code.tensapi.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAll() {
        return  ResponseEntity.ok(taskService.getAll());
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest request) {
        return new ResponseEntity<>(taskService.create(request), HttpStatus.CREATED);
    }
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody String request) {
        return new ResponseEntity<>(taskService.rec(request), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id,
                                               @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    @PostMapping("/task-user")
    public ResponseEntity<String> addUserToTask(@RequestParam Long taskId, @RequestParam Long userId){
        taskService.addUserToTask(taskId,userId);
        return ResponseEntity.ok("Add user to task");
    }

    @DeleteMapping("/task-user")
    public ResponseEntity<String> removeUserFromTask(@RequestParam Long taskId, @RequestParam Long userId){
        taskService.removeUserFromTask(taskId,userId);
        return ResponseEntity.ok("Remove user to task");
    }
}
