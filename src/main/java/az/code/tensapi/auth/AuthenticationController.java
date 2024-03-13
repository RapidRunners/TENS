package az.code.tensapi.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        AuthenticationResponse response = service.register(request);
        if (response == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", "Already registered");
            return new ResponseEntity<>(headers, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = service.authenticate(request);
        if (response == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", "Username or password is wrong");
            return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        AuthenticationResponse response = service.regenerateTokens(request);
        if (response == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", "Token is not valid");
            return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/confirm")
    public ResponseEntity<EmailConfirmationResponse> confirmEmail(@RequestParam("confirmationToken") String token) {
        EmailConfirmationResponse response = service.confirmEmail(token);
        if (response==null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", "Already confirmed");
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
