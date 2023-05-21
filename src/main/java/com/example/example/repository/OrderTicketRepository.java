package com.example.example.repository;

import com.example.example.domain.OrderTicketEntity;
import com.example.example.model.OrderTicketFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public interface OrderTicketRepository extends JpaRepository<OrderTicketEntity, Long>, JpaSpecificationExecutor<OrderTicketEntity> {

    Optional<OrderTicketEntity> findByOrderId(UUID orderId);

    default Page<OrderTicketEntity> filter(OrderTicketFilter filter, Pageable pageable) {

        Specification<OrderTicketEntity> specification = Specification.where(
                null
        );


        if (Objects.nonNull(filter.getStatuses())) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> root.get("ticketStatus").in(filter.getStatuses())
            );
        }

        return this.findAll(specification, pageable);

    }
}
