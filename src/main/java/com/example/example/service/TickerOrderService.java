package com.example.example.service;

import com.example.example.domain.OrderTicketEntity;
import com.example.example.domain.TicketStatus;
import com.example.example.repository.OrderTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TickerOrderService {


    private final OrderTicketRepository orderTicketRepository;


    @Transactional
    public OrderTicketEntity get(UUID orderId) {
        return this.orderTicketRepository.findByOrderId(orderId)
                .orElseGet(
                        () -> this.create(orderId)
                );
    }


    @Transactional
    public OrderTicketEntity create(UUID orderId) {
        return this.orderTicketRepository.save(
                new OrderTicketEntity()
                        .setOrderId(orderId)
                        .setTicketStatus(TicketStatus.OPEN)
                        .setDeadline(LocalDateTime.now().plusDays(7))
        );
    }


}
