package com.example.example.service.comment;

import com.example.example.client.CommentClient;
import com.example.example.model.CommentOwnerProblem;
import com.example.example.model.comment.CommentDto;
import com.example.example.model.comment.CommentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.example.model.comment.CommentType.*;

@Service
@RequiredArgsConstructor
public class CommentStatusOwnerProblemReference {

    private static final String MARKETPLACE_REF = "marketPlace";
    private static final String POSTAMAT_REF = "postamat";

    private static final Map<String, Set<CommentType>> WHO_IS_PROBLEM = Map.of(
            MARKETPLACE_REF, Set.of(
                    PRODUCT_DESCRIPTION,
                    PREPARE_ORDER,
                    GOT_ORDER,
                    PRODUCT,
                    DELIVERY,
                    NOTIFICATION
            ),
            POSTAMAT_REF, Set.of(
                    GETTING_ORDER,
                    GOT_ORDER
            )
    );

    private final CommentClient commentClient;


    public Map<UUID, Set<CommentOwnerProblem>> generate(Collection<UUID> commentIds) {
        return this.commentClient.getByIds(commentIds)
                .stream().collect(Collectors.toMap(CommentDto::getId, commentDto ->
                        Stream.of(
                                        containsMarketplaceTrouble(commentDto.getCommentTypesSet()) ? CommentOwnerProblem.MARKET_PLACE : null,
                                        containsPostamatTrouble(commentDto.getCommentTypesSet()) ? CommentOwnerProblem.POSTAMAT : null
                                )
                                .filter(Objects::nonNull).collect(Collectors.toSet())));

    }

    public boolean containsMarketplaceTrouble(Collection<CommentType> commentTypes) {
        return commentTypes.stream().anyMatch(this::isMarketplaceTrouble);
    }

    public boolean isMarketplaceTrouble(CommentType commentType) {
        return WHO_IS_PROBLEM.get(MARKETPLACE_REF).contains(commentType);
    }


    public boolean containsPostamatTrouble(Collection<CommentType> commentTypes) {
        return commentTypes.stream().anyMatch(this::isPostamatTrouble);
    }

    public boolean isPostamatTrouble(CommentType commentType) {
        return WHO_IS_PROBLEM.get(POSTAMAT_REF).contains(commentType);
    }

}
