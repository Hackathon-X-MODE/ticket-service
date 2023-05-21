package com.example.example.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Table(name = "comment_attachment_order_ticket")
@Entity
@ToString
@Accessors(chain = true)
public class CommentAttachmentOrderTicket {


    @Id
    private UUID commentId;


    private boolean processed;

    @ManyToOne
    @JoinColumn(columnDefinition = "order_ticket_id")
    private OrderTicketEntity ticket;

    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;
}
