package com.dev.test.ride_sharing_backend.Services;

import DTOs.RideDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.geo.Point;
import org.springframework.data.geo.Metrics;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    @Autowired
    private RedisTemplate<String, String> template;
    private final String GEO_KEY = "drivers:location";

    public String requestRide(RideDTO request){
        List<String> nearby = drivers(request);
        if (!nearby.isEmpty()) {
            String assignedDriver = nearby.get(0);
            template.opsForGeo().remove(GEO_KEY, assignedDriver);
            return assignedDriver;
        }
        return null;
    }

    public List<String>  drivers(RideDTO request){
        Point riderLocation = new Point(request.getLatitude(), request.getLongitude());
        Distance distance = new Distance(5, Metrics.KILOMETERS);

        GeoResults<RedisGeoCommands.GeoLocation<String>> results = template.opsForGeo().search(
                GEO_KEY,
                GeoReference.fromCoordinate(riderLocation),
                distance,
                RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs()
                        .sortAscending()
                        .limit(3)
        );


        if (results == null) return Collections.emptyList();

        return results.getContent().stream()
                .map(r -> r.getContent().getName())
                .collect(Collectors.toList());
    }

    public String requestStatus(Long id){
        return "ongoing";
    }
}
