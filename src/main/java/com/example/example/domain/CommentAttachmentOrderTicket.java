package com.example.example.domain;


import com.example.example.model.CommentOwnerProblem;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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


    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private CommentStatus status;

    @ManyToOne
    @JoinColumn(columnDefinition = "order_ticket_id")
    private OrderTicketEntity ticket;

    @Type(JsonType.class)
    @Column(name = "owner_problems", columnDefinition = "json")
    private Set<CommentOwnerProblemResolution> commentOwnerProblemResolutions;

    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentOwnerProblemResolution{

        private boolean resolved;

        private CommentOwnerProblem problem;
    }
}
