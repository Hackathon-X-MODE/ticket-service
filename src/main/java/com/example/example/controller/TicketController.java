package com.example.example.controller;

import com.example.example.model.OrderTicketDto;
import com.example.example.model.OrderTicketFilter;
import com.example.example.model.OrderTicketRegister;
import com.example.example.service.TickerOrderService;
import com.example.example.service.TicketOrderReceiverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class TicketController {

    private final TicketOrderReceiverService ticketOrderReceiverService;

    private final TickerOrderService tickerOrderService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void register(
            @RequestBody OrderTicketRegister orderTicketRegister
    ) {
        this.ticketOrderReceiverService.register(orderTicketRegister.getCommentId());
    }


    @GetMapping
    public Page<OrderTicketDto> get(
            OrderTicketFilter filter,
            Pageable pageable) {
        return this.tickerOrderService.getAll(filter, pageable);
    }

    @GetMapping("/{ticketId}")
    public OrderTicketDto get(
            @PathVariable("ticketId") Long ticketId
    ) {
        return this.tickerOrderService.get(ticketId);
    }
}
