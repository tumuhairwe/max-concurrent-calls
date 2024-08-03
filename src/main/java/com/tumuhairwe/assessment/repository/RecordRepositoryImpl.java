package com.tumuhairwe.assessment.repository;

import com.tumuhairwe.assessment.api.function.ModelMapperFunction;
import com.tumuhairwe.assessment.api.model.CallRecordResult;
import com.tumuhairwe.assessment.api.model.JPACallRecord;
import com.tumuhairwe.assessment.api.model.JSONCallRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecordRepositoryImpl {
    @Value("classpath:get_max_concurrent_calls_postgres.txt")
    private Resource postgresQuery;

    @Autowired
    private Connection connection;

    @Autowired
    private RecordRepository recordRepository;

//    @Autowired
//    private RecordRepositoryImpl recordRepository;
//
//    public List<CallRecordResult> getAnswer2(){
//        //1. Get all DISTINCT dates
//        Map<Integer, LocalDate> dates = recordRepository.findDistinctDates();
//
//        //2. for each date, get max concurrent-calls per customer -- sorted() by count (customerId and peakTimestamp)
//        for(Map.Entry<Integer, LocalDate> entry : dates.entrySet()){
//            int maxConcurrentCalls = recordRepository.findMaxCallsByDateAndCustomer(entry.getValue(), entry.getKey());
//        }
//        //3.
//        return new ArrayList<>();
//    }

    public String getSqlQuery() throws IOException {
        String contents;
        try (InputStream is = this.postgresQuery.getInputStream()) {
            contents = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }

        return contents;
    }

    public List<CallRecordResult> getRecords() throws IOException, SQLException {
        List<CallRecordResult> results = new ArrayList<>();
        PreparedStatement ps = this.connection.prepareStatement(getSqlQuery());
        try(ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                CallRecordResult record = new CallRecordResult();
                record.setDate(rs.getDate("peak_date"));
                record.setMaxConcurrentCalls(rs.getInt("max_concurrent_calls"));
                record.setCustomerId(rs.getInt("top_customer_id"));
                record.setTimestamp(rs.getTimestamp("peak_timestamp").toLocalDateTime().toEpochSecond(ZoneOffset.UTC));
                record.getCallIds().add(rs.getString("call_id_during_peak"));
            }
        }
        return results;
    }
    public List<JPACallRecord> doSave(JSONCallRecord record){
        Set<JPACallRecord> records = record.getCallRecords()
                .stream()
                .map(r -> new ModelMapperFunction().apply(r))
                .collect(Collectors.toSet());
        return this.recordRepository.saveAll(records);
    }
}
