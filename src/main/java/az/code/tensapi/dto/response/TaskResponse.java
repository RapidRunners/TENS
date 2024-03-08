package az.code.tensapi.dto.response;

import az.code.tensapi.entity.Category;
import az.code.tensapi.entity.Prioritize;
import az.code.tensapi.entity.Project;
import az.code.tensapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private String name;
    private String description;
    private LocalDate deadline;
    private Prioritize prioritize;
    // private List<User> accounts,
    private Category category;
    private  Project project;

}
