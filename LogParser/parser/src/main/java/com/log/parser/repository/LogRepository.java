package com.log.parser.repository;

import com.log.parser.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Repository Bean for LogEntity.
 * It contains all the methods to interact with the database.
 */
@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
    List<LogEntity> findAllByLogTimeStampBetween(Timestamp startTimestamp, Timestamp endTimestamp);

    List<LogEntity> findFirst10ByOrderByResponseTimeDesc();

    List<LogEntity> findAllByAuthStatus(String authStatus);

    List<LogEntity> findAllByHttpStatusCode(String httpStatusCode);
}
