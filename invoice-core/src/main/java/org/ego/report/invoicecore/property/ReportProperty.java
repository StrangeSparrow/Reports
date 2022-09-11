package org.ego.report.invoicecore.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "report")
public class ReportProperty {
    private String path;
}
