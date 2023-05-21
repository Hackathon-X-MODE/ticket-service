package com.example.example.controller;

import com.example.example.model.CommentOwnerProblem;
import com.example.example.model.SubmitTicketDto;
import com.example.example.service.comment.CommentTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/{ticketId}/comment")
@RequiredArgsConstructor
public class ActionCommentTicketController {

    private final CommentTicketService commentTicketService;


    @PutMapping("/{commentId}/confirm/{type}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void confirm(
            @PathVariable("ticketId") Long ticketId,
            @PathVariable("commentId") UUID commentId,
            @PathVariable("type") CommentOwnerProblem problem
    ) {
        this.commentTicketService.confirm(ticketId, commentId, problem);
    }
}
