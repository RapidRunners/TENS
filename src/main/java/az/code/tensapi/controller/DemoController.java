package az.code.tensapi.controller;

import az.code.tensapi.auth.AuthService;
import az.code.tensapi.auth.EmailConfirmationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DemoController {
    private final AuthService service;
    @GetMapping("/confirm")
    public ResponseEntity<EmailConfirmationResponse> confirmEmail(@RequestParam("confirmationToken") String token) {
        EmailConfirmationResponse response = service.confirmEmail(token);
        if (response==null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", "Error while confirming your email");
            return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello from secured endpoint");
    }
}
