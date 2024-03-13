package az.code.tensapi.service;

import az.code.tensapi.dto.request.TaskRequest;
import az.code.tensapi.dto.response.TaskResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface TaskService {
    List<TaskResponse> getAll();
    TaskResponse create(TaskRequest request);
    TaskResponse update(Long id, TaskRequest request);

    void delete(Long id);
    void removeUserFromTask(Long taskId, Long userId);
    void addUserToTask(Long taskId, Long userId) throws GeneralSecurityException, IOException;

}
