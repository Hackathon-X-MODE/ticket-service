package com.example.example.model.vendor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostamatDto {
    private UUID id;

    private UUID vendorId;

    //  private String externalId;

    // private Location location;

    //private int size;

    ///private LocalDateTime lastDateActivity;


    ///private URL videoLink;

    //private LocalDate postamatInit;
}
