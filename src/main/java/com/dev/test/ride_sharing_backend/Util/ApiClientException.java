package com.dev.test.ride_sharing_backend.Util;

public class ApiClientException {
    public static Throwable serverError(String message) {
        throw new RuntimeException(message);
    }

    public static Throwable authorizationError(String message) {
        throw new RuntimeException(message);
    }
}
