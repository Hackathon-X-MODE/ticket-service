package com.example.example.repository;

import com.example.example.domain.OrderTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderTicketRepository extends JpaRepository<OrderTicketEntity, Long> {

    Optional<OrderTicketEntity> findByOrderId(UUID orderId);
}
