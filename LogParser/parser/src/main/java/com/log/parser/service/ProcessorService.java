package com.log.parser.service;

import com.log.parser.dto.SuccessDTO;
import com.log.parser.entity.LogEntity;
import com.log.parser.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service class bean for processing log files
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessorService {
    private static final String[] SEARCH_LIST = {
            "IP-Address=",
            "!User-Agent=",
            "!X-Request-From=",
            "!Request-Type=",
            "!EnterpriseId=",
            "!EnterpriseName=",
            "!Auth-Status=",
            "!Status-Code=",
            "!Response-Time=",
            "!Request-Body=",
            "!API=",
            "#,",
    };
    private static final String[] REPLACEMENT_LIST = {"", "", "", "", "", "", "", "", "", "", "", ""};
    private static final Pattern m0 = Pattern.compile("IP-Address=.[^\\#]*");
    private static final Pattern m1 = Pattern.compile("!User-Agent=.[^\\#]*");
    private static final Pattern m2 = Pattern.compile("!X-Request-From=.[^\\#]*");
    private static final Pattern m3 = Pattern.compile("!Request-Type=.[^\\#]*");
    private static final Pattern m4 = Pattern.compile("!EnterpriseId=.[^\\#]*");
    private static final Pattern m5 = Pattern.compile("!EnterpriseName=.[^\\#]*");
    private static final Pattern m6 = Pattern.compile("!Auth-Status=.[^\\#]*");
    private static final Pattern m7 = Pattern.compile("!Status-Code=.[^\\#]*");
    private static final Pattern m8 = Pattern.compile("!Response-Time=.[^\\#]*");
    private static final Pattern m9 = Pattern.compile("!Request-Body=.[^\\#]*");
    private static final Pattern m10 = Pattern.compile("!API=.[^\\#]*");
    private static final List<Pattern> PATTERNS = Arrays.asList(m0, m1, m2, m3, m4, m5, m6, m7, m8, m9, m10);
    private final LogRepository logRepository;
    private String currentLog = "";
    private Long count = 0L;
    private Timestamp currentLogTimestamp;
    private ArrayList<LogEntity> logEntityArrayList = new ArrayList<>();

    /**
     * Method for processing log files
     *
     * @param multipartFile - log file to be parsed
     * @return - SuccessDTO object
     */
    public SuccessDTO<String> process(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        count = 0L;
        new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .forEach(this::handleLine);
        logRepository.saveAll(logEntityArrayList);
        return new SuccessDTO<>("Successfully processed " + count + " lines");
    }

    /**
     * Method for handling each line of log file
     *
     * @param line - line of log file
     */
    private void handleLine(String line) {
        if (line.contains("(default task")) {
            try {
                currentLogTimestamp = new Timestamp(DateUtils.parseDate(line.substring(0, 23), "yyyy-MM-dd hh:mm:ss,SSS").getTime());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            process(currentLog);
            currentLog = "";
        } else {
            currentLog += line;
        }

    }

    /**
     * Method for processing current log
     *
     * @param str - current log
     */
    private void process(String str) {
        List<String> result = PATTERNS.stream().map(pattern -> {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                count++;
                return currentLog.substring(matcher.start(), matcher.end());
            } else {
                return "";
            }
        }).collect(Collectors.toList());
        cleanse(result);
    }

    /**
     * Method for cleansing log data
     *
     * @param strings - list of strings
     */
    private void cleanse(List<String> strings) {
        List<String> cleansedArray = strings.stream().map(s -> {
            return StringUtils.replaceEach(s,
                    SEARCH_LIST,
                    REPLACEMENT_LIST
            );
        }).collect(Collectors.toList());
        LogEntity logEntity = new LogEntity(
                null,
                cleansedArray.get(0),
                cleansedArray.get(1),
                cleansedArray.get(2),
                cleansedArray.get(3),
                cleansedArray.get(4),
                cleansedArray.get(5),
                cleansedArray.get(6),
                cleansedArray.get(7),
                cleansedArray.get(8),
                cleansedArray.get(9),
                currentLogTimestamp,
                cleansedArray.get(10)
        );
        if (!Objects.equals(logEntity.getEnterpriseId(), "")) {
            logEntityArrayList.add(logEntity);
            if (logEntityArrayList.size() >= 50000) {
                logRepository.saveAll(logEntityArrayList);
                logEntityArrayList = new ArrayList<>();
            }
        }
    }

    public Page<LogEntity> getLogs(int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo, pageSize);
        return logRepository.findAll(pageable);
    }

    public List<LogEntity> filterByTimeStamp(String startTimeStamp, String endTimestamp) {
        Timestamp startTime = Timestamp.valueOf(startTimeStamp);
        Timestamp endTime = Timestamp.valueOf(endTimestamp);
        return logRepository.findAllByLogTimeStampBetween(startTime, endTime);
    }

    public List<LogEntity> topRequests() {
        return logRepository.findFirst10ByOrderByResponseTimeDesc();
    }

    public List<LogEntity> filterByStatusCode(String authStatus) {
        return logRepository.findAllByAuthStatus(authStatus);
    }

    public List<LogEntity> filterLogsByStatus(String status) {
        return logRepository.findAllByAuthStatus(status);
    }

    public List<LogEntity> filterLogsByHttpStatus(String httpStatusCode) {
        return logRepository.findAllByHttpStatusCode(httpStatusCode);
    }

    public LogEntity getLog(Long id) {
        return logRepository.findById(id).orElse(null);
    }
}
