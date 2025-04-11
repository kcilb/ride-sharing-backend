package com.dev.test.ride_sharing_backend.ApiSecurity;


import com.dev.test.ride_sharing_backend.AppConfigs.AppConfig;
import com.dev.test.ride_sharing_backend.Util.ApiClientException;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLException;

import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class ApiClient {
    private final AppConfig config;
    private String baseUrl;

    private HttpClient httpClient;

    public ApiClient(AppConfig config) {
        this.config = config;
    }

    @PostConstruct
    public void initSSLContext() {
        try {
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            httpClient = HttpClient.create()
                    .secure(t -> t.sslContext(sslContext))
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                    .responseTimeout(Duration.ofMillis(10000))
                    .doOnConnected(conn ->
                            conn.addHandlerLast(new ReadTimeoutHandler(10000, TimeUnit.MILLISECONDS))
                                    .addHandlerLast(new WriteTimeoutHandler(10000, TimeUnit.MILLISECONDS))
                    );
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }
    }


    public WebClient apiClient() {
        return WebClient.builder()
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(errorHandler());
                })
                .baseUrl(baseUrl)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }


    public WebClient.UriSpec<WebClient.RequestBodySpec> post() {
        baseUrl = config.mapAPI.replace("USER_KEY", config.mapAPIKey);
        return apiClient().post();
    }

    public Mono<Object> getCurrentLocation() {
        return post().uri("").bodyValue("").retrieve().bodyToMono(Object.class);
    }

    private ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().is5xxServerError()) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(ApiClientException.serverError(errorBody)));
            } else if (clientResponse.statusCode().is4xxClientError()) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(ApiClientException.authorizationError(errorBody)));
            } else {
                return Mono.just(clientResponse);
            }
        });
    }
}
