package org.ego.report.invoicecore.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    private String name;
    private byte[] data;
}
