package com.example.example.client;

import com.example.example.model.comment.CommentDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class CommentClient {


    private final WebClient webClient;

    public CommentClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://comment-service:" + Optional.ofNullable(System.getenv("SERVER_HTTP_PORT")).orElse("8083"))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


    public CommentDto getById(UUID commentId) {
        return Objects.requireNonNull(this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/{commentId}").build(commentId))
                .retrieve()
                .bodyToMono(CommentDto.class)
                .block());
    }
}
