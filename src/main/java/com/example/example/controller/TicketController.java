package com.example.example.controller;

import com.example.example.model.OrderTicketRegister;
import com.example.example.service.TicketOrderReceiverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class TicketController {

    private final TicketOrderReceiverService ticketOrderReceiverService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void register(
            @RequestBody OrderTicketRegister orderTicketRegister
    ) {
        this.ticketOrderReceiverService.register(orderTicketRegister.getCommentId());
    }
}
