package com.tumuhairwe.assessment.api.function;

import com.tumuhairwe.assessment.api.model.JPACallRecord;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MaxCallsPerDayFunction implements Function<List<JPACallRecord>, Map<LocalDate, Integer>> {

    @Override
    public Map<LocalDate, Integer> apply(List<JPACallRecord> callRecords) {
        Map<LocalDate, Integer> counts_by_date = new HashMap<>();

        for (JPACallRecord call : callRecords){

            if(call.isActiveOn(call.getStartDate())){
                counts_by_date.put(call.getStartDate(), counts_by_date.getOrDefault(call.getStartDate(), 0) + 1);
            }
            if(call.isActiveOn(call.getEndDate())){
                counts_by_date.put(call.getStartDate(), counts_by_date.getOrDefault(call.getEndDate(), 0) + 1);
            }
        }

        return counts_by_date;
    }
}
