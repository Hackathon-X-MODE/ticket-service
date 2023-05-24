package com.example.example.service;

import com.example.example.domain.CommentAttachmentOrderTicket;
import com.example.example.domain.CommentStatus;
import com.example.example.domain.TicketStatus;
import com.example.example.model.SubmitTicketDto;
import com.example.example.model.comment.CommentDto;
import com.example.example.model.comment.CommentMood;
import com.example.example.service.status.TicketStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketOrderReceiverService {

    private final TicketOrderService ticketOrderService;

    private final TicketStatusService ticketStatusService;

    @Transactional
    public void notify(CommentDto comment) {
        log.info("Receive comment {}", comment.getId());

        if (comment.getMood() != CommentMood.NEGATIVE && comment.getRate() > 3) {
            log.info("Comment is more positive, ignore");
            this.ticketOrderService.findByOrderId(comment.getOrderId()).ifPresent(
                    ticket -> ticket.getComments().removeIf(commentTicket -> commentTicket.getCommentId().equals(comment.getId()))
            );
            return;
        }

        final var ticket = this.ticketOrderService.getDto(comment.getOrderId());

        if (ticket.getComments().stream().noneMatch(commentTicket -> commentTicket.getCommentId().equals(comment.getId()))) {
            ticket.getComments().add(
                    new CommentAttachmentOrderTicket()
                            .setTicket(ticket)
                            .setStatus(CommentStatus.NOT_PROCESSED)
                            .setCommentId(comment.getId())
            );
            log.info("Comment {} attached to {}", comment.getId(), ticket.getId());
            if (ticket.getTicketStatus() != TicketStatus.OPEN) {
                ticketStatusService.changeStatus(ticket, SubmitTicketDto.builder().build(), TicketStatus.OPEN);
            }
        }
    }
}
