package az.code.tensapi.dto.request;

import az.code.tensapi.entity.Prioritize;
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
        private Prioritize prioritize;
        private Long categoryId;
        private Long projectId;

}
