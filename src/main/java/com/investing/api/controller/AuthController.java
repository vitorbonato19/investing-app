package com.investing.api.controller;

import com.investing.api.entity.dto.LoginRequestDto;
import com.investing.api.entity.dto.LoginResponseDto;
import com.investing.api.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.status(200).body(authService.login(request));
    }
}
