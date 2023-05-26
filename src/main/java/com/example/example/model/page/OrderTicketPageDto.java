package com.example.example.model.page;

import com.example.example.domain.TicketStatus;
import com.example.example.model.CommentAttachDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTicketPageDto {

    private Long id;

    private TicketStatus ticketStatus;

    private OrderDto order;

    private List<CommentAttachDto> comments;

    private LocalDateTime deadline;

    private LocalDateTime createDate;

}
