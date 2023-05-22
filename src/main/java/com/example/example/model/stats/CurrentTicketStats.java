package com.example.example.model.stats;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CurrentTicketStats {

    @Schema(description = "Открытых без просроченного дедлайн'а")
    private long open;

    @Schema(description = "Ожидающие внешних систем")
    private long pending;

    @Schema(description = "Открытых у которых просрочен дедлайн")
    private long deadline;
}
