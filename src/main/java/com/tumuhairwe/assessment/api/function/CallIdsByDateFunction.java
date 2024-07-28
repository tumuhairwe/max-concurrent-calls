package com.tumuhairwe.assessment.api.function;

import com.tumuhairwe.assessment.api.model.JPACallRecord;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CallIdsByDateFunction implements Function<List<JPACallRecord>, Map<LocalDateTime, List<String>>> {
    @Override
    public Map<LocalDateTime, List<String>> apply(List<JPACallRecord> callRecords) {
        Map<LocalDateTime, List<String>> callIds_by_date = new HashMap<>();

        for (JPACallRecord call : callRecords){

            if(call.isActiveOn(call.getStartDate())){
                callIds_by_date.putIfAbsent(call.getStartTimestamp(), new ArrayList<>());
                callIds_by_date.get(call.getStartDate()).add(call.getId().getCallId());
            }
            if(call.isActiveOn(call.getEndDate())){
                callIds_by_date.putIfAbsent(call.getEndTimestamp(), new ArrayList<>());
                callIds_by_date.get(call.getEndDate()).add(call.getId().getCallId());
            }
        }

        return callIds_by_date;
    }
}
