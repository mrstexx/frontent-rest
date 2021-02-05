package com.postingg.app.auth;

import com.postingg.app.auth.email.MailService;
import com.postingg.app.auth.email.NotificationMail;
import com.postingg.app.auth.token.ConfirmationToken;
import com.postingg.app.auth.token.ConfirmationTokenRepository;
import com.postingg.app.user.User;
import com.postingg.app.user.UserRepository;
import com.postingg.app.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final MailService mailService;

    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        boolean isUsernameTaken = userRepository.findByUsername(registerRequest.getUsername()).isPresent();
        if (isUsernameTaken) {
            throw new IllegalStateException("Username is already taken");
        }
        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                UserRole.USER);

        // sava a new user
        userRepository.save(user);

        // generate a token
        String token = generateVerificationToken(user);

        // and send a token to confirm it via email
        mailService.sendMail(
                new NotificationMail(
                        "Please Activate your Account",
                        user.getEmail(),
                        "Thank you for signing up to Postingg. Please click on the URL below to activate your account: " +
                                "http://localhost:8080/api/auth/confirm?token=" + token));
    }

    private String generateVerificationToken(User user) {
        String verificationToken = UUID.randomUUID().toString();
        // TODO: Read from config file minutes
        ConfirmationToken token = new ConfirmationToken(
                verificationToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user);
        confirmationTokenRepository.save(token);
        return verificationToken;
    }

    @Transactional
    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }
        LocalDateTime expiresAt = confirmationToken.getExpiresAt();
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }
        confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
        userRepository.enableUser(confirmationToken.getUser().getUsername());
    }
}
