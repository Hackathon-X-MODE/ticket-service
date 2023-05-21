package com.example.example.service.comment;

import com.example.example.domain.CommentAttachmentOrderTicket;
import com.example.example.domain.TicketStatus;
import com.example.example.exception.EntityNotFoundException;
import com.example.example.model.CommentOwnerProblem;
import com.example.example.model.SubmitTicketDto;
import com.example.example.service.TicketOrderService;
import com.example.example.service.status.TicketStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentTicketService {

    private final TicketOrderService ticketOrderService;

    private final TicketStatusService ticketStatusService;

    @Transactional
    public void confirm(Long ticketId, UUID commentId, CommentOwnerProblem problem) {
        final var ticket = this.ticketOrderService.get(ticketId);

        final var comment = ticket.getComments().stream().filter(commentAttachmentOrderTicket -> commentAttachmentOrderTicket.getCommentId().equals(commentId))
                .findFirst().orElseThrow(
                        () -> new EntityNotFoundException("Can't find comment " + commentId + " at ticket " + ticketId)
                );
        final var resolutions = comment.getCommentOwnerProblemResolutions();
        resolutions.stream().filter(resolution -> resolution.getProblem().equals(problem))
                .findFirst()
                .map(resolution -> resolution.setResolved(true));

        if (resolutions.stream().allMatch(CommentAttachmentOrderTicket.CommentOwnerProblemResolution::isResolved)) {
            this.ticketStatusService.changeStatus(ticket, SubmitTicketDto.builder().build(), TicketStatus.COMPLETED);
        }
    }
}
