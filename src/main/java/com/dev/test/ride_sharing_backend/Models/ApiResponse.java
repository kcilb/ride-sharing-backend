package com.dev.test.ride_sharing_backend.Models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class ApiResponse<T> implements Serializable {

    private T data;
    private Response response;

    public static <T> ApiResponse<T> formatResponse(T data, Response response){
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.response = response;
        apiResponse.data = data;
        return apiResponse;
    }
}
