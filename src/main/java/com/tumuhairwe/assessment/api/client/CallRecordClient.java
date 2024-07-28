package com.tumuhairwe.assessment.api.client;

import com.tumuhairwe.assessment.api.model.CallRecord;
import com.tumuhairwe.assessment.api.model.CallRecordResultWrapper;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "callRecordClient",url = "https://candidate.hubteam.com/candidateTest/v3/")
public interface CallRecordClient {

    @RequestMapping(method = RequestMethod.GET, value = "problem/test-dataset", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Headers("Content-Type: application/json")
    CallRecord getCallRecords(@Param("userKey") String userKey);

    @RequestMapping(method = RequestMethod.GET, value = "problem/dataset",  produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Headers("Content-Type: application/json")
    CallRecord getTestCallRecords(@Param("userKey") String userKey);

    @PostMapping(value = "problem/result", produces = "application/json")
    @Headers("Content-Type: application/json")
    CallRecord sendProdResult(@RequestBody CallRecordResultWrapper payload);

    @PostMapping(value = "problem/test-result", produces = "application/json")
    @Headers("Content-Type: application/json")
    CallRecord sendTestResult(@RequestBody CallRecordResultWrapper payload);
}
