package com.tumuhairwe.assessment.api.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CallRecordResultWrapper {

    private List<CallRecordResult> results;

    public CallRecordResultWrapper(){
        this.results = new ArrayList<>();
    }
}
