package com.log.parser.resource;

import com.log.parser.dto.SuccessDTO;
import com.log.parser.entity.LogEntity;
import com.log.parser.service.ProcessorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


/**
 * LogResource: Contains all the endpoints for the log parser
 * The endpoints are:
 * 1. /logs/upload - uploads a file
 * 2. /logs - returns all the logs
 * 3. /logs/{id} - returns the log with the given id
 * 4. /logs/auth-status - returns the logs with given auth-status
 * 5. /logs/http-status - returns the logs with given httpStatus
 * 6. /logs-between-timestamp - returns the logs between given timestamp
 * 7. /most-response-time - returns top 10 logs with most response time
 */
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Slf4j
public class LogResource {

    private final ProcessorService processorService;

    @PostMapping(value = "/logs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessDTO<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        long start = System.currentTimeMillis();
        log.info("Processing Start ");
        var response = processorService.process(file);

        long time = System.currentTimeMillis() - start;
        log.info("Processing End. Took :" + time / 1000 + " seconds to process the file");
        return response;
    }

    @GetMapping("/logs")
    public Page<LogEntity> getLogs(@RequestParam("page") int page, @RequestParam("size") int size) {
        return processorService.getLogs(page, size);
    }

    @GetMapping("/logs/{id}")
    public LogEntity getLog(@PathVariable("id") Long id) {
        return processorService.getLog(id);
    }

    @GetMapping("/logs/auth-status")
    public List<LogEntity> filterLogs(@RequestParam("authStatus") String status) {
        return processorService.filterLogsByStatus(status);
    }

    @GetMapping("/logs/http-status")
    public List<LogEntity> filterByHttpStatus(@RequestParam("httpStatus") String httpStatus) {
        return processorService.filterLogsByHttpStatus(httpStatus);
    }

    @GetMapping(value = "/logs-between-timestamp")
    public List<LogEntity> filterLogs(
            @RequestParam String startTimestamp,
            @RequestParam String endTimestamp
    ) {
        return processorService.filterByTimeStamp(startTimestamp, endTimestamp);
    }

    @GetMapping(value = "/most-response-time")
    public List<LogEntity> topRequests() {
        return processorService.topRequests();
    }
}