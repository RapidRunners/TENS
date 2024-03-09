package az.code.tensapi.dto.response;

import az.code.tensapi.entity.Category;
import az.code.tensapi.entity.Priority;
import az.code.tensapi.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private String name;
    private String description;
    private LocalDate deadline;
    private Priority prioritize;
    private String summary;
    private Category category;
    private Project project;

}
