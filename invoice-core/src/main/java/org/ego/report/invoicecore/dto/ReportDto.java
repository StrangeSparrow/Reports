package org.ego.report.invoicecore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReportDto {
    private String name;
    private LocalDateTime creationDate;
}
