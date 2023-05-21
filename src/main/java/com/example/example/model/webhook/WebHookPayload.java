package com.example.example.model.webhook;

import com.example.example.model.CommentOwnerProblem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebHookPayload {

    private String url;

    private String orderId;

    private UUID commentId;

    private String comment;

    private CommentOwnerProblem problem;
}
