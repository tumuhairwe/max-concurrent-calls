package com.tumuhairwe.assessment.controller;

import com.tumuhairwe.assessment.api.model.JPACallRecord;
import com.tumuhairwe.assessment.api.model.JSONCallRecord;
import com.tumuhairwe.assessment.api.service.CallRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class CallRecordController {

    @Autowired
    private CallRecordService service;

    @GetMapping("/api/test")
    public JSONCallRecord getCallRecords() throws IOException {
        return this.service.readTestCallRecords();
    }
    @PostMapping("/api/save")
    public List<JPACallRecord> saveCallRecords() throws IOException {

        JSONCallRecord record = this.service.readTestCallRecords();
        return this.service.doSave(record);
    }
}
