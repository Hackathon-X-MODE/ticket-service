package com.example.example.service.stats;

import com.example.example.domain.TicketStatus;
import com.example.example.model.stats.CurrentTicketStats;
import com.example.example.model.stats.TicketPerDays;
import com.example.example.repository.OrderTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketStatsService {


    private final OrderTicketRepository orderTicketRepository;


    @Transactional(readOnly = true)
    public List<TicketPerDays> getPerDay(LocalDate from) {
        return this.orderTicketRepository.getPerDays(from).stream().map(tuple ->
                TicketPerDays.builder()
                        .count(tuple.get(0, Long.class))
                        .localDate(tuple.get(1, Date.class).toLocalDate())
                        .build()
        ).toList();
    }

    @Transactional(readOnly = true)
    public CurrentTicketStats getStatus() {
        final var currentDate = LocalDateTime.now();
        return CurrentTicketStats.builder()
                .open(this.orderTicketRepository.countAllByTicketStatusAndDeadlineGreaterThanEqual(TicketStatus.OPEN, currentDate))
                .pending(this.orderTicketRepository.countAllByTicketStatus(TicketStatus.PENDING))
                .deadline(this.orderTicketRepository.countAllByTicketStatusAndDeadlineLessThanEqual(TicketStatus.OPEN, currentDate))
                .build();
    }
}
