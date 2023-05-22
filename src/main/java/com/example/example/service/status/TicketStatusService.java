package com.example.example.service.status;

import com.example.example.domain.OrderTicketEntity;
import com.example.example.domain.TicketStatus;
import com.example.example.model.SubmitTicketDto;
import com.example.example.service.TicketOrderService;
import com.example.example.service.status.handler.TicketStatusHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TicketStatusService {

    private static final Map<TicketStatus, Set<TicketStatus>> TICKET_STATUS_FLOW = Map.of(
            TicketStatus.OPEN, Set.of(TicketStatus.PENDING, TicketStatus.CANCELED),
            TicketStatus.PENDING, Set.of(TicketStatus.CANCELED, TicketStatus.COMPLETED, TicketStatus.OPEN),
            TicketStatus.CANCELED, Set.of(TicketStatus.OPEN),
            TicketStatus.COMPLETED, Set.of(TicketStatus.OPEN)
    );

    private final TicketOrderService ticketOrderService;

    private final List<TicketStatusHandler> ticketStatusHandlerList;

    @Transactional
    public void confirm(Long ticketId, SubmitTicketDto submitTicketDto) {
        final var ticket = this.ticketOrderService.get(ticketId);

        this.changeStatus(ticket, submitTicketDto, TicketStatus.PENDING);
    }


    public void changeStatus(OrderTicketEntity ticket, SubmitTicketDto submitTicketDto, TicketStatus ticketStatus) {
        final var currentStatus = ticket.getTicketStatus();
        final var flow = TICKET_STATUS_FLOW.get(currentStatus);

        if (flow == null || !flow.contains(ticketStatus)) {
            throw new IllegalArgumentException("Can't change status " + currentStatus + " to " + ticketStatus);
        }

        ticket.setTicketStatus(ticketStatus);
        this.ticketStatusHandlerList.forEach(ticketStatusHandler -> ticketStatusHandler.handle(ticket, submitTicketDto, ticketStatus));

    }
}
