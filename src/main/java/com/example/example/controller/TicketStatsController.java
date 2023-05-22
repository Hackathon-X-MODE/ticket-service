package com.example.example.controller;

import com.example.example.model.stats.CurrentTicketStats;
import com.example.example.model.stats.TicketPerDays;
import com.example.example.service.stats.TicketStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class TicketStatsController {

    private final TicketStatsService ticketStatsService;

    @GetMapping("perDay")
    public List<TicketPerDays> getPerDay(
            @RequestParam("from") LocalDate from
    ) {
        return this.ticketStatsService.getPerDay(from);
    }

    @GetMapping("status")
    public CurrentTicketStats getStatus() {
        return this.ticketStatsService.getStatus();
    }
}
