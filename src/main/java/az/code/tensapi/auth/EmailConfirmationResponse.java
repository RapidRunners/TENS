package az.code.tensapi.auth;

import lombok.Builder;

@Builder
public class EmailConfirmationResponse {
    private String email;
    private boolean confirmed;
}
