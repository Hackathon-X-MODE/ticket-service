package com.example.example.repository;

import com.example.example.domain.OrderTicketEntity;
import com.example.example.domain.TicketStatus;
import com.example.example.model.OrderTicketFilter;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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


    @Query(value = """
            select count(*), create_date\\:\\:timestamp\\:\\:date from order_ticket
            where create_date >= :fromDate
            group by create_date\\:\\:timestamp\\:\\:date
            order by create_date asc;
            """, nativeQuery = true)
    List<Tuple> getPerDays(@Param("fromDate") LocalDate fromDate);


    long countAllByTicketStatusAndDeadlineGreaterThanEqual(TicketStatus ticketStatus, LocalDateTime deadline);
    long countAllByTicketStatusAndDeadlineLessThanEqual(TicketStatus ticketStatus, LocalDateTime deadline);
    long countAllByTicketStatus(TicketStatus ticketStatus);
}
