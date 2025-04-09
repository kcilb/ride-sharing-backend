package com.dev.test.ride_sharing_backend.Controllers;

import DTOs.AuthDTO;
import com.dev.test.ride_sharing_backend.JwtUtil.JwtUtil;
import com.dev.test.ride_sharing_backend.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apis/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody AuthDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
