package com.dev.test.ride_sharing_backend.Controllers;


import DTOs.UserDTO;
import com.dev.test.ride_sharing_backend.Services.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis/v1/external")
public class ExternalController {

    @Autowired
    ExternalService externalService;

    @PostMapping(value = "find-location",produces = "application/json")
    public ResponseEntity<?> findLocation(@RequestBody UserDTO request) {
        return ResponseEntity.ok(externalService.findLocation(request).block());
    }
}
