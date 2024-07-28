package com.tumuhairwe.assessment.api.function;

import com.tumuhairwe.assessment.api.model.JPACallRecord;
import com.tumuhairwe.assessment.api.model.JPACallRecordId;
import com.tumuhairwe.assessment.api.model.JSONCallId;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Function;

public class ModelMapperFunction implements Function<JSONCallId, JPACallRecord> {
    @Override
    public JPACallRecord apply(JSONCallId r) {
        JPACallRecord cr = new JPACallRecord();

        Instant start = Instant.ofEpochMilli(r.getStartTimestamp());
        LocalDateTime startTime = LocalDateTime.ofInstant(start, ZoneId.of("UTC"));
        cr.setStartTimestamp(startTime);

        Instant end = Instant.ofEpochMilli(r.getEndTimestamp());
        LocalDateTime endTime = LocalDateTime.ofInstant(end, ZoneId.of("UTC"));
        cr.setEndTimestamp(endTime);

        JPACallRecordId i = new JPACallRecordId();
        i.setCallId(r.getCallId());
        i.setCustomerId(r.getCustomerId());
        cr.setId(i);

        return cr;
    }
}
