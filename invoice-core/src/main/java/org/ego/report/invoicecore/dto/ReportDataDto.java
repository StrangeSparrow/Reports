package org.ego.report.invoicecore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportDataDto {
    private String name;
    private byte[] data;
}
