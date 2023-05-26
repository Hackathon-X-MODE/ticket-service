package com.example.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Table(name = "order_ticket")
@Entity
@ToString
@Accessors(chain = true)
public class OrderTicketEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private TicketStatus ticketStatus;

    @Column(name = "order_id", nullable = false, unique = true)
    private UUID orderId;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentAttachmentOrderTicket> comments = new ArrayList<>();

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "endDate")
    private LocalDateTime endDate;

    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTicketEntity that = (OrderTicketEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
