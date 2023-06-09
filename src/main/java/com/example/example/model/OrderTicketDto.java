package com.example.example.model;

import com.example.example.domain.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTicketDto {

    private Long id;

    private TicketStatus ticketStatus;

    private UUID orderId;

    private List<CommentAttachDto> comments;

    private LocalDateTime deadline;

    private LocalDateTime createDate;

}
