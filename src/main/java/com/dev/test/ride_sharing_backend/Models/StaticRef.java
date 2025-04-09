package com.dev.test.ride_sharing_backend.Models;

import org.springframework.stereotype.Component;

@Component
public class StaticRef {

    public static Response success() {
        return new Response("0", "Request Has Been Processed Successfully");
    }

    public static Response noRecordFound() {
        return new Response("404", "No Records Found");
    }

    public static Response failed() {
        return new Response("-11", "Operation Failed");
    }

    public static Response customMessage(String code,String message) {
        return new Response(code, message);
    }

    public static Response loginMessage(String message) {
        return new Response("0", message);
    }

    public static Response serverError() {
        return new Response("403", "Error occurred while processing request");
    }
}
