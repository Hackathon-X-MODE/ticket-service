package com.example.example.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderClient {


    private final WebClient webClient;

    public OrderClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://order-service:" + Optional.ofNullable(System.getenv("SERVER_HTTP_PORT")).orElse("8081"))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


    public UUID getOrderIdByKey(String key) {
        return Objects.requireNonNull(this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/refs/{key}").build(key))
                .retrieve()
                .bodyToMono(Dto.class)
                .block()).id;
    }

    public void deleteKey(String key) {
        this.webClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/refs/{key}").build(key))
                .retrieve()
                .toBodilessEntity()
                .block();
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Dto {
        UUID id;
    }
}
