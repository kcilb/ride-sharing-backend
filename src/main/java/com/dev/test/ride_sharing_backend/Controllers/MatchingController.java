package com.dev.test.ride_sharing_backend.Controllers;

import DTOs.AuthDTO;
import DTOs.RideDTO;
import com.dev.test.ride_sharing_backend.Services.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis/v1/matching")
public class MatchingController {

    @Autowired
    MatchingService matchingService;

    @PostMapping(value = "/request-ride", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> requestRide(@RequestBody RideDTO request) {
        return ResponseEntity.ok(matchingService.requestRide(request));
    }

    @GetMapping(value = "/drivers", produces = "application/json")
    public ResponseEntity<?> drivers(@RequestBody RideDTO request) {
        return ResponseEntity.ok(matchingService.drivers(request));
    }

    @GetMapping(value = "/ride-status", produces = "application/json")
    public ResponseEntity<?> rideStatus(@RequestParam("id") Long id) {
        return ResponseEntity.ok(matchingService.requestStatus(id));
    }
}
