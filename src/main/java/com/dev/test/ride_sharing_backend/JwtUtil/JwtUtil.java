package com.dev.test.ride_sharing_backend.JwtUtil;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Autowired
    private RSAPrivateKey privateKey;
    @Autowired
    private RSAPublicKey publicKey;

    public String generateToken(String subject) {
        String jwtToken = null;
        try {
            Map<String, String> claims = new HashMap<>();
            claims.put("audience", "USERS");
            claims.put("issuer", "ride_sharing");
            jwtToken = createJwtForClaims(subject, claims);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        return jwtToken;
    }

    private String createJwtForClaims(String subject, Map<String, String> claims) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Instant.now().toEpochMilli());
        calendar.add(Calendar.DATE, 1);
        JWTCreator.Builder jwtBuilder = JWT.create().withSubject(subject);
        claims.forEach(jwtBuilder::withClaim);
        return jwtBuilder
                .withNotBefore(new Date())
                .withExpiresAt(calendar.getTime())
                .sign(Algorithm.RSA256(publicKey, privateKey));
    }
}
