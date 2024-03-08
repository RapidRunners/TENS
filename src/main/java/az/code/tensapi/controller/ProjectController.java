package az.code.tensapi.controller;

import az.code.tensapi.dto.request.CategoryRequest;
import az.code.tensapi.dto.request.ProjectRequest;
import az.code.tensapi.dto.response.CategoryResponse;
import az.code.tensapi.dto.response.ProjectResponse;
import az.code.tensapi.service.CategoryService;
import az.code.tensapi.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> findAll() {
        return new ResponseEntity<>(projectService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> findById(@PathVariable Long projectId) {
        return new ResponseEntity<>(projectService.findById(projectId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> save(@RequestBody ProjectRequest request) {
        return new ResponseEntity<>(projectService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> update(@PathVariable Long projectId,
                                                  @RequestBody ProjectRequest request) {
        return new ResponseEntity<>(projectService.updateCategory(projectId, request), HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    public void delete(@PathVariable Long projectId) {
        projectService.delete(projectId);
    }
}
