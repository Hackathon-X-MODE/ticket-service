package com.example.example.controller;

import com.example.example.model.OrderTicketDto;
import com.example.example.model.OrderTicketFilter;
import com.example.example.model.page.OrderTicketPageDto;
import com.example.example.service.TicketOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TicketController {

    private final TicketOrderService ticketOrderService;



    @GetMapping
    public Page<OrderTicketPageDto> get(
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
