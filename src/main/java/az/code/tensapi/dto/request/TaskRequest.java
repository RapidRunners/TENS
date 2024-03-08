package az.code.tensapi.dto.request;

import az.code.tensapi.entity.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
        private String name;
        private String description;
        private LocalDate deadline;
        private Priority prioritize;
        private Long categoryId;
        private Long projectId;

}
