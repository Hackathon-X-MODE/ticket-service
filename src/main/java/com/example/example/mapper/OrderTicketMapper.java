package com.example.example.mapper;

import com.example.example.configuration.MapperConfiguration;
import com.example.example.domain.OrderTicketEntity;
import com.example.example.model.OrderTicketDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface OrderTicketMapper {


    OrderTicketDto toDto(OrderTicketEntity orderTicketEntity);
}
