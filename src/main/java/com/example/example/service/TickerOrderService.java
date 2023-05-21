package com.example.example.service;

import com.example.example.domain.OrderTicketEntity;
import com.example.example.domain.TicketStatus;
import com.example.example.exception.EntityNotFoundException;
import com.example.example.mapper.OrderTicketMapper;
import com.example.example.model.OrderTicketDto;
import com.example.example.model.OrderTicketFilter;
import com.example.example.repository.OrderTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TickerOrderService {


    private final OrderTicketMapper orderTicketMapper;

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


    @Transactional(readOnly = true)
    public Page<OrderTicketDto> getAll(OrderTicketFilter filter, Pageable pageable) {
        final var page = this.orderTicketRepository.filter(filter, pageable);

        return new PageImpl<>(
                page.stream().map(this.orderTicketMapper::toDto)
                        .toList(),
                pageable, page.getTotalElements()
        );
    }


    @Transactional(readOnly = true)
    public OrderTicketDto get(Long id) {
        return this.orderTicketMapper.toDto(
                this.orderTicketRepository.findById(id)
                        .orElseThrow(
                                () -> new EntityNotFoundException("Can't find ticket-order " + id)
                        )
        );
    }


}
