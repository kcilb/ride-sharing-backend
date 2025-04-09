package com.dev.test.ride_sharing_backend.Services;

import DTOs.AuthDTO;
import com.dev.test.ride_sharing_backend.JwtUtil.JwtUtil;
import com.dev.test.ride_sharing_backend.Models.ApiResponse;
import com.dev.test.ride_sharing_backend.Models.StaticRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    public ApiResponse<String> login(AuthDTO request) {
        try {
            if ("admin".equals(request.getUsername()) && "password".equals(request.getPassword())) {
                String token = jwtUtil.generateToken(request.getUsername());
                return ApiResponse.formatResponse(token, StaticRef.success());
            } else {
                return ApiResponse.formatResponse(null,
                        StaticRef.customMessage("403", "Invalid credentials"));
            }
        } catch (Exception e) {
            return ApiResponse.formatResponse(null,
                    StaticRef.serverError());
        }
    }
}
