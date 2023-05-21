package com.example.example.service.status.handler;

import com.example.example.client.CommentClient;
import com.example.example.client.OrderClient;
import com.example.example.client.VendorClient;
import com.example.example.domain.CommentAttachmentOrderTicket;
import com.example.example.domain.CommentStatus;
import com.example.example.domain.OrderTicketEntity;
import com.example.example.domain.TicketStatus;
import com.example.example.model.CommentAttachDto;
import com.example.example.model.CommentOwnerProblem;
import com.example.example.model.SubmitTicketDto;
import com.example.example.model.order.OrderWithMetaDto;
import com.example.example.model.vendor.VendorDto;
import com.example.example.model.webhook.WebHookPayload;
import com.example.example.service.sender.WebHookService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sorry for that :( Deadline coming...
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PendingTicketStatusHandler implements TicketStatusHandler {


    private final OrderClient orderClient;

    private final VendorClient vendorClient;

    private final CommentClient commentClient;

    private final WebHookService webHookService;

    @Override
    public void handle(@NonNull OrderTicketEntity ticket, @NonNull SubmitTicketDto submitTicketDto, @NonNull TicketStatus newStatus) {
        if (newStatus != TicketStatus.PENDING) {
            return;
        }


        final var notProcessedComments = ticket.getComments().stream()
                .filter(comment -> comment.getStatus() == CommentStatus.NOT_PROCESSED)
                .collect(Collectors.toSet());

        final var submitComments = submitTicketDto.getSolve().stream().collect(Collectors.toMap(CommentAttachDto::getId, CommentAttachDto::getProblemOwners));

        if (notProcessedComments.size() != submitComments.values().size()) {
            throw new IllegalArgumentException("Please present all comments with not processed");
        }

        if (notProcessedComments.isEmpty()) {
            log.warn("Ticket {} do pending but without tasks", ticket.getId());
            return;
        }


        //Processing Entity... fill resolutions
        notProcessedComments.forEach(notProcessedComment -> {

            notProcessedComment.setStatus(CommentStatus.SENT);
            notProcessedComment.setCommentOwnerProblemResolutions(
                    submitComments.get(notProcessedComment.getCommentId()).stream().map(problem ->
                            CommentAttachmentOrderTicket.CommentOwnerProblemResolution.builder()
                                    .problem(problem)
                                    .resolved(false)
                                    .build()
                    ).collect(Collectors.toSet())
            );
        });


        //Generate webhook
        final var comments = this.commentClient.getByIds(notProcessedComments.stream().map(CommentAttachmentOrderTicket::getCommentId).collect(Collectors.toSet()));
        final var order = this.orderClient.getOrderById(ticket.getOrderId());

        final var vendorContext = VendorContext.builder()
                .order(order.getVendorId())
                .postamat(this.getPostamatVendor(order))
                .build();

        vendorContext.init(this.vendorClient.getVendorsByIds(vendorContext.getVendors()));


        this.webHookService.sendPayload(
                comments.stream().flatMap(comment ->
                                this.commentOwnerProblems(comment.getId(), submitTicketDto).stream().map(commentOwnerProblem ->
                                        WebHookPayload.builder()
                                                .url(vendorContext.webHookFor(commentOwnerProblem))
                                                .orderId(order.getExternalId())
                                                .commentId(comment.getId())
                                                .comment(comment.getComment())
                                                .problem(commentOwnerProblem)
                                                .build()
                                )
                        )
                        .toList()
        );
    }

    private Set<CommentOwnerProblem> commentOwnerProblems(UUID commentId, SubmitTicketDto submitTicketDto) {
        return submitTicketDto.getSolve().stream().filter(commentAttachDto -> commentAttachDto.getId().equals(commentId)).findFirst()
                .map(CommentAttachDto::getProblemOwners)
                .orElse(Set.of());
    }

    @Nullable
    private UUID getPostamatVendor(OrderWithMetaDto order) {
        if (order.getPostamatId() != null) {
            return this.vendorClient.getPostamatById(order.getPostamatId()).getVendorId();
        }
        return null;
    }

    @Data
    @Builder
    @AllArgsConstructor
    static class VendorContext {
        private final UUID order;

        private Optional<VendorDto> orderVendor;


        private final UUID postamat;

        private Optional<VendorDto> postamatVendor;


        public Set<UUID> getVendors() {
            return Stream.of(
                            this.order,
                            this.postamat
                    ).filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }


        public void init(List<VendorDto> vendorDtoList) {
            this.orderVendor = vendorDtoList.stream().filter(vendorDto -> vendorDto.getId().equals(this.order)).findFirst();
            this.postamatVendor = vendorDtoList.stream().filter(vendorDto -> vendorDto.getId().equals(this.postamat)).findFirst();
        }

        public String webHookFor(CommentOwnerProblem commentOwnerProblem) {
            return switch (commentOwnerProblem) {
                case MARKET_PLACE -> this.orderVendor.map(VendorDto::getWebhook).orElse(null);
                case POSTAMAT -> this.postamatVendor.map(VendorDto::getWebhook).orElse(null);
            };
        }


    }
}
