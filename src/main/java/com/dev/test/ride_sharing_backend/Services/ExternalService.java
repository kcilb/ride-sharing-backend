package com.dev.test.ride_sharing_backend.Services;

import DTOs.UserDTO;
import com.dev.test.ride_sharing_backend.ApiSecurity.ApiClient;
import com.dev.test.ride_sharing_backend.Models.ApiResponse;
import com.dev.test.ride_sharing_backend.Models.StaticRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ExternalService {

    @Autowired
    ApiClient apiClient;

    public Mono<ApiResponse<Map<String, Object>>> findLocation(UserDTO request) {
        return apiClient.getCurrentLocation()
                .map(result -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> responseMap = (Map<String, Object>) result;
                    return ApiResponse.formatResponse(responseMap, StaticRef.success());
                })
                .onErrorResume(e -> Mono.just(ApiResponse.formatResponse(null, StaticRef.serverError())));
    }

}
