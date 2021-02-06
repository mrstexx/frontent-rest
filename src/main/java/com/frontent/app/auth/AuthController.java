package com.frontent.app.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest) {
        authService.signUp(registerRequest);
        return new ResponseEntity<>("User registration was successful", HttpStatus.OK);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token) {
        authService.confirmToken(token);
        return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
    }

}
