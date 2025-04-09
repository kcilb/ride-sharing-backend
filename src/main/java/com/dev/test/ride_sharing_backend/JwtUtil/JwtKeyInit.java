package com.dev.test.ride_sharing_backend.JwtUtil;


import com.dev.test.ride_sharing_backend.AppConfigs.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@RequiredArgsConstructor
public class JwtKeyInit {

    private final AppConfig appConfig;

    @Bean
    public KeyStore keyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream resourceAsStream = new FileInputStream(appConfig.getKeyPath());
            if (resourceAsStream == null) {
                throw new RuntimeException("Keystore file not found: " + appConfig.getKeyPath());
            }

            keyStore.load(resourceAsStream, appConfig.keyPassword.toCharArray());
            return keyStore;
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new RuntimeException("Unable to load keystore: {}" + appConfig.getKeyPath());
        }
    }

    @Bean
    public RSAPrivateKey jwtSigningKey(KeyStore keyStore) {
        try {
            Key key = keyStore.getKey(appConfig.keyAlias,
                    appConfig.keyPassphrase.toCharArray());
            if (key instanceof RSAPrivateKey) {
                return (RSAPrivateKey) key;
            }
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new RuntimeException("Unable to load private key from keystore: {}" + appConfig.keyPath);
        }
        throw new IllegalArgumentException("Unable to load private key");
    }

    @Bean
    public RSAPublicKey jwtValidationKey(KeyStore keyStore) {
        try {
            java.security.cert.Certificate cert = (Certificate) keyStore.getCertificate(appConfig.keyAlias);
            PublicKey publicKey = cert.getPublicKey();

            if (cert instanceof X509Certificate x509Cert) {
            }

            if (publicKey instanceof RSAPublicKey) {
                return (RSAPublicKey) publicKey;
            }
        } catch (KeyStoreException e) {
            throw new RuntimeException("Unable to load public key from keystore: {}" + appConfig.keyPath);
        }
        throw new IllegalArgumentException("Unable to load RSA public key");
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }
}