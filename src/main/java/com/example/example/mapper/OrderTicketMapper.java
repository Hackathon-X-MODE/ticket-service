package com.example.example.mapper;

import com.example.example.configuration.MapperConfiguration;
import com.example.example.domain.CommentAttachmentOrderTicket;
import com.example.example.domain.OrderTicketEntity;
import com.example.example.model.CommentAttachDto;
import com.example.example.model.OrderTicketDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface OrderTicketMapper {

    OrderTicketDto toDto(OrderTicketEntity orderTicketEntity);


    @Mapping(target = "id", source = "commentId")
    @Mapping(target = "problemOwners", ignore = true)
    @Mapping(target = "commentOwnerProblemResolutions", source = "commentOwnerProblemResolutions")
    CommentAttachDto toDto(CommentAttachmentOrderTicket commentAttachmentOrderTicket);



}
