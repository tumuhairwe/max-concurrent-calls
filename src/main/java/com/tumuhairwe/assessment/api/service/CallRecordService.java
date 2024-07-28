package com.tumuhairwe.assessment.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tumuhairwe.assessment.api.client.CallRecordClient;
import com.tumuhairwe.assessment.api.function.ModelMapperFunction;
import com.tumuhairwe.assessment.repository.RecordRepository;
import com.tumuhairwe.assessment.api.model.CallRecordResult;
import com.tumuhairwe.assessment.api.model.JPACallRecord;
import com.tumuhairwe.assessment.api.model.JSONCallId;
import com.tumuhairwe.assessment.api.model.JSONCallRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CallRecordService {

    @Autowired
    private CallRecordClient client;

    @Value("${userKey}")
    private String userKey;

    @Value("classpath:records.json")
    private File file;

    @Autowired
    private RecordRepository recordRepository;

    public List<JPACallRecord> getTestCallRecords(){
        return client.getTestCallRecords(userKey).getCallRecords();
    }

    public JSONCallRecord readTestCallRecords() throws IOException {
        JSONCallRecord myObjects = new ObjectMapper().readValue(file, JSONCallRecord.class);
        return myObjects;
    }

    public List<JPACallRecord> doSave(JSONCallRecord record){
        Set<JPACallRecord> records = record.getCallRecords()
                .stream()
                .map(r -> new ModelMapperFunction().apply(r))
                .collect(Collectors.toSet());
        return this.recordRepository.saveAll(records);
    }

    //Status: Incomplete
    public List<CallRecordResult> getAnswer(){
        List<Map<String, Object>> results = this.recordRepository.getCount();

        List<CallRecordResult> answers = new ArrayList<>();
        for (Map<String, Object> result : results){
            int count = Integer.parseInt(result.get("count_by_date").toString());
            int customerId = Integer.parseInt(result.get("customer_id").toString());
            String callId = result.get("call_id").toString();
            ZonedDateTime start_timestamp = ZonedDateTime.parse(result.get("start_timestamp").toString());
            ZonedDateTime end_timestamp = ZonedDateTime.parse(result.get("end_timestamp").toString());

            // this would be problematic if start <= midnight && end > midnight (i.e. peak is over night)
            LocalDate peakDate = start_timestamp.toLocalDate();
            int hourOfStart = start_timestamp.getHour();
            int hourOfEnd = end_timestamp.getHour();
            Integer peak = 0;

//            if(!(hourOfStart < midnight && hourOfEnd > midnight)){
//                peak = recordRepository.getPeakCountByCustomerAndDate(customerId, start_timestamp.toLocalDateTime(), end_timestamp.toLocalDateTime());
//            }

            CallRecordResult a = new CallRecordResult();
            a.setCustomerId(customerId);
            a.setMaxConcurrentCalls(count);
            //a.setMaxConcurrentCalls(peak);
            a.setDate(start_timestamp.toLocalDate());
            a.getCallIds().add(callId);

            answers.add(a);
        }
        return answers;
    }



    // customerId
    // maxConcurrentCall
    // timestamp

    //  the maximum number of concurrent calls for each customer for each day.
//    public CallRecordResultWrapper doCalculate(){
//        //List<CallRecord> records = client.getCallRecords(userKey).getCallRecords();
//        List<CallRecord> records = client.getTestCallRecords(userKey).getCallRecords();
//        //List<CallRecord> recs = new ObjectMapper().readValue(file, List)
//
//        //List<CallRecord> myObjects = new ObjectMapper().readValue(file, new TypeReference<List<CallRecord>>(){});
//        Map<LocalDate, List<CallRecord>> grouping = getCallsGroupedByStartDate(records);
//        //Map<LocalDate, Answer> grouping = getCallsGroupedByStartDate(records);
//
//        CallRecordResultWrapper wrapper = new CallRecordResultWrapper();
//
//        // calc counts by date
//        Map<LocalDate, Integer> counts_by_date = new MaxCallsPerDayFunction().apply(records);
//        Map<LocalDate, List<String>> customerId_by_date = new CallIdsByDate().apply(records);
//        //Map<LocalDate, Integer> callId_by_date = new MaxCallsPerDayFunction().apply(records);
//
//        Map<LocalDate, CallRecordResult> counts_by_result = new HashMap<>();
//
//        CallRecordResult prev = new CallRecordResult();
//        for (Map.Entry<LocalDate, Integer> customer : counts_by_date.entrySet()) {
//            prev.setMaxConcurrentCalls(customer.getValue()); // set count
//            prev.setDate(customer.getKey());
//        }

//        // cal
//        // Map<LocalDate, Integer> result = new HashMap<>();
//        for (Map.Entry<LocalDate, List<CallRecord>> entry : grouping.entrySet()) {
//            if (counts_by_date.containsKey(entry.getKey())) {
//                Answer answer = new Answer();
//                answer.setDate(entry.getKey());  // set date
//                answer.setMaxConcurrentCalls(entry.getValue(entry));
//                //wrapper.getResults().add(answer);
//            }
//        }
//        //0. transform into DailyCustomerRecord
//        //Map<LocalDate, List<CallRecord>> grouping = getCallsGroupedByStartDate(records);
//
//        //1. for each customer ... calc max number of calls
//        //Map<LocalDate, Integer> counts = new HashMap();  // Date, customerId, max
//
//        for (Map.Entry<LocalDate, List<CallRecord>> customer : grouping.entrySet()){
//            CallRecordResult r = new CallRecordResult();
//            r.setDate(customer.getKey());
//            //r.setMaxConcurrentCalls();
//            //r.setCustomerId();
//            wrapper.getResults();
//        }
//
//        CallRecordResultWrapper wrapper = new CallRecordResultWrapper();
//        for (Map.Entry<LocalDate, Integer> entry : map.entrySet()){
//            CallRecordResult r = new CallRecordResult();
//            r.setDate(entry.getKey());
//
//            Answer answer = new Answer();
//            answer.setDate(entry.getKey());  // set date
//            //answer.getCallId().add();
//            answer.setMaxConcurrentCalls(map.get(entry));
//            //wrapper.getResults().add(answer);
//        }
//
//        return wrapper;
//    }
//
    public Map<Integer, List<JSONCallId>> getCallsGroupedByCustomer(List<JSONCallId> records){
        // customerId
        // max
        Map<Integer, List<JSONCallId>> callsGroupedByCustomer = records.stream()
                .collect(Collectors.groupingBy(JSONCallId::getCustomerId));

        return callsGroupedByCustomer;
    }

    public Map<LocalDate, List<JSONCallId>> getCallsGroupedByStartDate(List<JSONCallId> records){
        Map<LocalDate, List<JSONCallId>> callsGroupedByCustomer = records.stream()
                .collect(Collectors.groupingBy(JSONCallId::getStartDate));

        return callsGroupedByCustomer;
    }

}
