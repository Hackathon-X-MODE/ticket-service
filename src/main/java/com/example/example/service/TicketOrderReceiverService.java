package com.example.example.service;

import com.example.example.client.CommentClient;
import com.example.example.domain.CommentAttachmentOrderTicket;
import com.example.example.domain.CommentStatus;
import com.example.example.model.comment.CommentMood;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketOrderReceiverService {

    private final CommentClient commentClient;

    private final TicketOrderService ticketOrderService;

    @Transactional
    public void register(UUID commentId) {
        log.info("Receive comment {}", commentId);
        final var comment = this.commentClient.getById(commentId);

        if (comment.getMood() != CommentMood.NEGATIVE && comment.getRate() > 3) {
            log.info("Comment is more positive, ignore");
            return;
        }

        final var ticket = this.ticketOrderService.getDto(comment.getOrderId());

        if (ticket.getComments().stream().noneMatch(commentTicket -> commentTicket.getCommentId().equals(commentId))) {
            ticket.getComments().add(
                    new CommentAttachmentOrderTicket()
                            .setTicket(ticket)
                            .setStatus(CommentStatus.NOT_PROCESSED)
                            .setCommentId(commentId)
            );
            log.info("Comment {} attached to {}", commentId, ticket.getId());
        }
    }
}
