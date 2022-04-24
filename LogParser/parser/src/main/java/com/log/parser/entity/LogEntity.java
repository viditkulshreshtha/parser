package com.log.parser.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.sql.Timestamp;

/**
 * Entity class for log entity
 */

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "seq")
    private Long id;
    private String ipAddress;
    private String userAgent;
    private String requestFrom;
    private String requestType;
    private String enterpriseId;
    private String enterpriseName;
    private String authStatus;
    private String httpStatusCode;
    private String responseTime;
    private String requestBody;
    private Timestamp logTimeStamp;
    private String api;
}
