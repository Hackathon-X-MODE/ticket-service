package com.example.example.service.status.handler;

import com.example.example.domain.OrderTicketEntity;
import com.example.example.domain.TicketStatus;
import com.example.example.model.SubmitTicketDto;
import org.springframework.lang.NonNull;

public interface TicketStatusHandler {

    void handle(@NonNull OrderTicketEntity ticket, @NonNull SubmitTicketDto submitTicketDto, @NonNull TicketStatus newStatus);
}
