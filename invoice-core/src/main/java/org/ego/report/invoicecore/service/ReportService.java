package org.ego.report.invoicecore.service;

import lombok.RequiredArgsConstructor;
import org.ego.report.invoicecore.dto.ReportDto;
import org.ego.report.invoicecore.exception.ReportFindException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReportService {
    @Value("${report.path}")
    private String path;
    private Path pathToReports;

    @PostConstruct
    void init() {
        pathToReports = Path.of(path);
    }

    public Flux<ReportDto> findAll() {
        try {
            Stream<Path> files = Files.list(pathToReports);
            return Flux.fromStream(files)
                    .filter(p -> !Files.isDirectory(p))
                    .map(Path::toFile)
                    .map(f -> new ReportDto(f.getName(), LocalDateTime.ofInstant(Instant.ofEpochMilli(f.lastModified()), ZoneId.systemDefault())))
                    .doOnComplete(files::close);
        } catch (IOException e) {
            throw new ReportFindException("Path of reports is invalid", e);
        }
    }
}
