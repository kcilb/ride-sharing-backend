package com.dev.test.ride_sharing_backend.Services;

import DTOs.RideDTO;
import org.springframework.stereotype.Service;

@Service
public class MatchingService {

    public String requestRide(RideDTO request){
        return "ride";
    }

    public String drivers(){
        return "drivers";
    }

    public String requestStatus(Long id){
        return "ongoing";
    }
}
