package az.code.tensapi.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailConfirmationResponse {
    private String email;
    private boolean confirmed;
}
