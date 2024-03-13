package az.code.tensapi.auth;

import az.code.tensapi.config.JwtService;
import az.code.tensapi.entity.ConfirmationToken;
import az.code.tensapi.entity.Role;
import az.code.tensapi.entity.User;
import az.code.tensapi.repository.UserRepo;
import az.code.tensapi.service.EmailService;
import az.code.tensapi.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent())
            return null;

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .verified(false)
                .build();
        userRepo.save(user);

        emailService.sendConfirmationEmail(
                user.getEmail(),
                confirmationTokenService.generateConfirmationToken(user)
                        .getToken());

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepo.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse regenerateTokens(RefreshTokenRequest request) {
//        String accessToken = request.getAccessToken();
        String refreshToken = request.getRefreshToken();
        if (refreshToken == null)
            return null;

        if (!jwtService.isExpired(refreshToken)
                && jwtService.extractTokenType(refreshToken).equalsIgnoreCase("refresh")) {
            String username = jwtService.extractUsername(refreshToken);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            String newAccessToken = jwtService.generateAccessToken(userDetails);
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);

            return AuthenticationResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        }
        return null;
    }

    public EmailConfirmationResponse confirmEmail(String request) {
        ConfirmationToken token = confirmationTokenService.getByToken(request);
        if (token == null)
            return null;

        String email = token.getUser().getEmail();
        User user = userRepo.findByEmail(email).orElseThrow();
        user.setVerified(true);
        userRepo.save(user);

        confirmationTokenService.delete(token);
        return EmailConfirmationResponse
                .builder()
                .confirmed(true)
                .email(email)
                .build();
    }
}
