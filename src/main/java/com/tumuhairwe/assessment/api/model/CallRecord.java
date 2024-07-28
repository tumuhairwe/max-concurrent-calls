package com.tumuhairwe.assessment.api.model;

import lombok.Data;

import java.util.List;

@Data
public class CallRecord {

    private List<JPACallRecord> callRecords;
}
