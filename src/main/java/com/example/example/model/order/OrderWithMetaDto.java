package com.example.example.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderWithMetaDto {

    private UUID id;

    private String externalId;

    private UUID vendorId;
    private UUID postamatId;

    //  private BigDecimal sum;

    // private StatusOrder status;

    // private Person person;

    //  private List<String> categories;

    // private OrderMeta meta;

    // private URL ref;

    // private DateHistory dateHistory;
}
