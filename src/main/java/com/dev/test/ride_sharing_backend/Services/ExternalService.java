package com.dev.test.ride_sharing_backend.Services;

import DTOs.UserDTO;
import com.dev.test.ride_sharing_backend.Models.ApiResponse;
import com.dev.test.ride_sharing_backend.Models.StaticRef;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExternalService {

    public ApiResponse<Map<String, Object>> findLocation(UserDTO request) {
        try {
            //check lat and longitude if they exist in cache if the do, do not make API call
            return ApiResponse.formatResponse(null, StaticRef.success());
        } catch (Exception e) {
            return ApiResponse.formatResponse(null, StaticRef.serverError());
        }
    }
}
