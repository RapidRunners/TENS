package az.code.tensapi.service;

import az.code.tensapi.entity.ConfirmationToken;
import az.code.tensapi.entity.User;

import java.util.Optional;

public interface ConfirmationTokenService {
    ConfirmationToken generateConfirmationToken(User user);
    ConfirmationToken getByToken(String token);
    void delete(ConfirmationToken token);
}
