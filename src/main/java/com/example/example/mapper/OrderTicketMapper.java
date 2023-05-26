package com.example.example.mapper;

import com.example.example.configuration.MapperConfiguration;
import com.example.example.domain.CommentAttachmentOrderTicket;
import com.example.example.domain.OrderTicketEntity;
import com.example.example.model.CommentAttachDto;
import com.example.example.model.OrderTicketDto;
import com.example.example.model.order.OrderWithMetaDto;
import com.example.example.model.page.OrderDto;
import com.example.example.model.page.OrderTicketPageDto;
import com.example.example.model.page.VendorDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface OrderTicketMapper {

    OrderTicketDto toDto(OrderTicketEntity orderTicketEntity);


    @Mapping(target = "id", source = "commentId")
    @Mapping(target = "problemOwners", ignore = true)
    @Mapping(target = "commentOwnerProblemResolutions", source = "commentOwnerProblemResolutions")
    CommentAttachDto toDto(CommentAttachmentOrderTicket commentAttachmentOrderTicket);



    OrderTicketPageDto toPageDto(OrderTicketEntity orderTicketEntity);

    OrderDto toPage(OrderWithMetaDto orderWithMetaDto);


    VendorDto toPageVendor(com.example.example.model.vendor.VendorDto vendorDto);
}
