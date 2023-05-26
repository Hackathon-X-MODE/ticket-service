package com.example.example.service;

import com.example.example.client.OrderClient;
import com.example.example.client.VendorClient;
import com.example.example.domain.CommentStatus;
import com.example.example.domain.OrderTicketEntity;
import com.example.example.domain.TicketStatus;
import com.example.example.exception.EntityNotFoundException;
import com.example.example.mapper.OrderTicketMapper;
import com.example.example.model.CommentAttachDto;
import com.example.example.model.OrderTicketDto;
import com.example.example.model.OrderTicketFilter;
import com.example.example.model.order.OrderWithMetaDto;
import com.example.example.model.page.OrderTicketPageDto;
import com.example.example.model.vendor.VendorDto;
import com.example.example.repository.OrderTicketRepository;
import com.example.example.service.comment.CommentStatusOwnerProblemReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketOrderService {


    private final OrderTicketMapper orderTicketMapper;

    private final OrderTicketRepository orderTicketRepository;

    private final CommentStatusOwnerProblemReference commentStatusOwnerProblemReference;

    private final VendorClient vendorClient;

    private final OrderClient orderClient;


    @Transactional
    public OrderTicketEntity getDto(UUID orderId) {
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
    public Page<OrderTicketPageDto> getAll(OrderTicketFilter filter, Pageable pageable) {
        final var page = this.orderTicketRepository.filter(filter, pageable);

        final var result = page.stream().toList();

        final var orders = this.orderClient.getOrders(
                result.stream().map(OrderTicketEntity::getOrderId).collect(Collectors.toSet())
        );

        final var vendors = this.vendorClient.getVendorsByIds(
                orders.values().stream().map(OrderWithMetaDto::getVendorId).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(VendorDto::getId, Function.identity()));


        return new PageImpl<>(
                result.stream().map(r -> {
                    final var dto = this.orderTicketMapper.toPageDto(r);
                    dto.setOrder(this.orderTicketMapper.toPage(orders.get(r.getOrderId())));
                    dto.getOrder().setVendor(
                            this.orderTicketMapper.toPageVendor(vendors.get(dto.getOrder().getVendorId()))
                    );
                    return dto;
                }).toList(),
                pageable, page.getTotalElements()
        );
    }


    @Transactional(readOnly = true)
    public OrderTicketDto getDto(Long id) {
        final var data = this.orderTicketMapper.toDto(this.get(id));

        final var notPendingStatus = data.getComments().stream()
                .filter(commentAttachDto -> commentAttachDto.getStatus() == CommentStatus.NOT_PROCESSED)
                .collect(Collectors.toSet());
        log.info("Generate problems for {}", notPendingStatus);

        final var map = this.commentStatusOwnerProblemReference.generate(
                notPendingStatus.stream().map(CommentAttachDto::getId).collect(Collectors.toSet())
        );

        notPendingStatus.forEach(commentAttachDto -> commentAttachDto.setProblemOwners(map.get(commentAttachDto.getId())));

        return data;
    }


    @Transactional(readOnly = true)
    public Optional<OrderTicketEntity> findByOrderId(UUID orderId) {
        return this.orderTicketRepository.findByOrderId(orderId);
    }

    @Transactional(readOnly = true)
    public OrderTicketEntity get(Long id) {
        return this.orderTicketRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find ticket-order " + id)
                );
    }


}
