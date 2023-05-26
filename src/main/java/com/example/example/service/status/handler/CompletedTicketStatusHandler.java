package com.example.example.service.status.handler;

import com.example.example.domain.OrderTicketEntity;
import com.example.example.domain.TicketStatus;
import com.example.example.model.SubmitTicketDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Sorry for that :( Deadline coming...
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CompletedTicketStatusHandler implements TicketStatusHandler {

    @Override
    public void handle(@NonNull OrderTicketEntity ticket, @NonNull SubmitTicketDto submitTicketDto, @NonNull TicketStatus newStatus) {
        if (newStatus != TicketStatus.COMPLETED) {
            return;
        }
        ticket.setEndDate(LocalDateTime.now());
    }
}
