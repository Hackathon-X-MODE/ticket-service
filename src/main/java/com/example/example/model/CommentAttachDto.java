package com.example.example.model;

import com.example.example.domain.CommentAttachmentOrderTicket;
import com.example.example.domain.CommentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentAttachDto {

    private UUID id;

    private CommentStatus status;

    @Schema(description = "Автоматический подбор источников проблем")
    private Set<CommentOwnerProblem> problemOwners;

    @Schema(description = "Выставленные источники проблемы")
    private Set<CommentAttachmentOrderTicket.CommentOwnerProblemResolution> commentOwnerProblemResolutions;
}
