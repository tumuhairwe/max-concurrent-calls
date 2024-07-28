package com.tumuhairwe.assessment.api.model;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class JSONCallId implements Serializable {
    private int customerId;
    private String callId;
    private long startTimestamp;
    private long endTimestamp;

    public LocalDate getStartDate() {
        Instant start = Instant.ofEpochMilli(startTimestamp);
        LocalDateTime startTime = LocalDateTime.ofInstant(start, ZoneId.of("UTC"));
        return startTime.toLocalDate();
    }
}
