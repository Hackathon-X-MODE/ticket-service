package com.example.example.controller;

import com.example.example.model.OrderTicketDto;
import com.example.example.model.OrderTicketFilter;
import com.example.example.model.comment.CommentDto;
import com.example.example.service.TicketOrderReceiverService;
import com.example.example.service.TicketOrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TicketController {

    private final TicketOrderReceiverService ticketOrderReceiverService;

    private final TicketOrderService ticketOrderService;

    @Operation(summary = "Уведомить сервис об изменениях в комментарии", description = "Для переданного комментария будет создан")
    @PostMapping("/notify")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void notify(@RequestBody CommentDto commentDto) {
        this.ticketOrderReceiverService.notify(commentDto);
    }


    @GetMapping
    public Page<OrderTicketDto> get(
            OrderTicketFilter filter,
            Pageable pageable) {
        return this.ticketOrderService.getAll(filter, pageable);
    }

    @GetMapping("/{ticketId}")
    public OrderTicketDto get(
            @PathVariable("ticketId") Long ticketId
    ) {
        return this.ticketOrderService.getDto(ticketId);
    }
}
