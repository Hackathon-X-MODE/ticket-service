package com.example.example.client;

import com.example.example.model.vendor.PostamatDto;
import com.example.example.model.vendor.VendorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Component
public class VendorClient {


    private final WebClient webClient;

    public VendorClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://vendor-service:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


    public List<VendorDto> getVendorsByIds(Set<UUID> vendorIds) {
        return Objects.requireNonNull(this.webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/list").build())
                .bodyValue(vendorIds)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<VendorDto>>() {
                })
                .block());
    }


    public PostamatDto getPostamatById(UUID postamatId) {
        return Objects.requireNonNull(this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/postamates/{postamatId}").build(postamatId))
                .retrieve()
                .bodyToMono(PostamatDto.class)
                .block());
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
