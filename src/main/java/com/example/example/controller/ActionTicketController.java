package com.example.example.controller;

import com.example.example.model.SubmitTicketDto;
import com.example.example.service.status.TicketStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ActionTicketController {

    private final TicketStatusService ticketStatusService;


    @PutMapping("/{ticketId}/confirm")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void confirm(
            @PathVariable("ticketId") Long ticketId,
            @RequestBody SubmitTicketDto submitTicketDto
    ) {
        this.ticketStatusService.confirm(ticketId, submitTicketDto);
    }

}
