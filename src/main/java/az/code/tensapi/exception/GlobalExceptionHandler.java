package az.code.tensapi.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleCategoryNotFoundException(CategoryNotFoundException ex,
                                                                            WebRequest req) {
        ex.printStackTrace();

        return ResponseEntity.status(400).body(ErrorResponseDto.builder()
                .status(400)
                .title("Exception")
                .details("Category not found")
                .build());
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleProjectNotFoundException(ProjectNotFoundException ex,
                                                                           WebRequest req) {
        ex.printStackTrace();

        return ResponseEntity.status(400).body(ErrorResponseDto.builder()
                .status(400)
                .title("Exception")
                .details("Project not found")
                .build());
    }
}
