package com.tumuhairwe.assessment.api.model;

import lombok.Data;

import java.util.List;

@Data
public class CallRecordResponse {

    private List<CallRecordResult> results;
}
