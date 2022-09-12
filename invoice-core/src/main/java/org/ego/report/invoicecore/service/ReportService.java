package org.ego.report.invoicecore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ego.report.invoicecore.dto.ReportDataDto;
import org.ego.report.invoicecore.dto.ReportInfoDto;
import org.ego.report.invoicecore.exception.ReportFindException;
import org.ego.report.invoicecore.exception.ReportSaveException;
import org.ego.report.invoicecore.model.Report;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {
    @Value("${report.path}")
    private String path;
    private Path pathToReports;

    @PostConstruct
    private void init() {
        pathToReports = Path.of(path);
    }

    public Flux<ReportInfoDto> findAll() {
        log.info("[Report Service] - Find all reports");
        try {
            Stream<Path> files = Files.list(pathToReports);
            return Flux.fromStream(files)
                    .filter(p -> !Files.isDirectory(p))
                    .map(Path::toFile)
                    .map(f -> new ReportInfoDto(f.getName(), LocalDateTime.ofInstant(Instant.ofEpochMilli(f.lastModified()), ZoneId.systemDefault())))
                    .doOnError(e -> files.close())
                    .doOnComplete(files::close);
        } catch (IOException e) {
            log.error("[Report Service] - Path of reports - '{}' - is invalid", path, e);
            throw new ReportFindException("Path of reports is invalid", e);
        }
    }

    public void save(Report report) {
        log.info("[Report Service] - Save report: {}", report.getName());
        Path pathToFile = Paths.get(path, report.getName());
        try {
            Files.write(pathToFile, report.getData());
            log.info("[Report Service] - Report saved: {}", report.getName());
        } catch (IOException e) {
            log.error("[Report Service] - Report doesn't save: {}", report.getName(), e);
            throw new ReportSaveException(String.format("Report doesn't save: %s", report.getName()), e);
        }
    }

    public Mono<ReportDataDto> findReport(String name) {
        log.debug("[Report Service] - Download report: {}", name);
        return Mono.fromCallable(() -> {
            Path pathToFile = Paths.get(path, name);
            File file = pathToFile.toFile();
            try {
                if (file.exists() && file.isFile())
                    return new ReportDataDto(name, Files.readAllBytes(pathToFile));
            } catch (IOException e) {
                log.error("[Report Service] - Failed to download report: {}", name, e);
                throw new ReportFindException("Failed to download report", e);
            }
            log.error("[Report Service] - Report doesn't exist: {}", name);
            throw new ReportFindException("Report doesn't exist");
        });
    }
}
