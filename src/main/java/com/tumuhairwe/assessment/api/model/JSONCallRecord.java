package com.tumuhairwe.assessment.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JSONCallRecord implements Serializable {
    private List<JSONCallId> callRecords;
}
