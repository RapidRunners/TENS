package az.code.tensapi.auth;

import lombok.Data;

import java.util.UUID;
@Data
public class EmailConfirmationRequest {
    private String confirmationToken;
}
