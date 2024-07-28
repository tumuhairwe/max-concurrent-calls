package com.tumuhairwe.assessment.api.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class CallRecordResult {

    private int customerId;
    private String date;    // yyyy-dd-dd
    private int maxConcurrentCalls;
    private long timestamp;
    private List<String> callIds;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-DD");
    public void setDate(LocalDate date) {
        this.date = date.format(formatter);
    }

    public void setFormattedDate(LocalDate date){
        this.date = date.format(formatter);
    }
}
