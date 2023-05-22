package com.example.example.model.stats;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketPerDays {

    @Schema(description = "Открытых за день")
    private long count;

    @Schema(description = "День.")
    private LocalDate localDate;
}
