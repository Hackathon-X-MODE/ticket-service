package com.example.example.controller.internal;

import com.example.example.model.comment.CommentDto;
import com.example.example.service.TicketOrderReceiverService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private")
@RequiredArgsConstructor
public class TicketInternalController {

    private final TicketOrderReceiverService ticketOrderReceiverService;

    @Operation(
            summary = "Сервисный метод для уведомления сервиса об изменениях в комментарии",
            description = "Для переданного комментария будет создан тикет, если тикет не подходит, будет удален."
    )
    @PostMapping("/notify")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void notify(@RequestBody CommentDto commentDto) {
        this.ticketOrderReceiverService.notify(commentDto);
    }
}
