package org.ego.report.invoicecore.controller;

import lombok.RequiredArgsConstructor;
import org.ego.report.invoicecore.dto.ReportInfoDto;
import org.ego.report.invoicecore.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    @GetMapping
    public Flux<ReportInfoDto> findAll() {
        return reportService.findAll();
    }
}
