## Incoming schema
```
{
    callRecords : [
        {
        "customerId": int,
        "callId": "UUID",
        "startTimestamp": long,
        "endTimestamp": long
        }
    ]
}
```
Task: Convert incoming data to outgoing schema

## Outgoing schema
```
{
    results : [
        {
        "customerId": int,
        "date": "UUID",
        "maxConcurrentCalls": long,
        "callIds" : [
            UUID
        ]
        "timestamp": long
        }
    ]
}
```
## Algorithm
- GET data from Hubspot API
- Aggregate the records by date  (select ... GROUP BY CAST(startTimestamp AS DATE) )
- find timestamp that is peak for each day i.e. "peak_of_day" (Map - see entry schema below)
```
  key = date (yyyy-MM-dd)
  value = {
        - callCount (int)
        - timestamp (long)
    }
```

- Find peak for each customer (Map - see entry schema below)
 ```
  key = customerId (UUID or String)
  value: peak_timestamp (long)
```
- If customer's peak within peak_of_day.timestamp -> add customerId to Map
```
    { 
        key = date (yyy-MM-d)
        value = { 
            customerIds : List<Integer>, 
            peak_time: timestamp
            maxConcurrentCalls: int
        }
     }
```

- Transform map to outgoing schema

STATUS
- DONE: 
    - Fetch data from Hubspot API
    - Create domain object (see api.model package: JSONCallRecord)
    - Implement Feign client (see api.client package: CallRecordClient)
    - ~~Implement Service to perform aggregation programmatically~~(Java Stream API)
      ~~Implement Service to perform aggregation programmatically~~(JsonPath or JsonATA API)
    - Implement Controller to fetch data (see controller package: HubspotController.getCallRecords() )
    - Implement Controller to save data to database (going to with database-based aggregation) see package: HubspotController.saveCallRecords() )
    - Implement logic to perform aggregation: (see function package)    
- IN_PROGRESS
  - Implement HubspotService logic to call SQL queries in HubspotRepository to fetch aggregated data

LESSON LEARNED
- Implement aggregation programmatically (Java streaming API groupBy) 
  - This was a mistake because while I'm most comfortable with the streaming API, I under-estimated how much time it would take to get it right for this usecase's corner edges (e.g. if peak crosses past midnight and one have to)
  - See commented out code in HubspotService (this attempt ate up all my time which is why I couldn't finish in time)
- Implement aggregation via DB : Timeseries DB (e.g. Influx)
  - For a real-word use case this would be the ideal solution
  - I spent some time researching the Influx query API and how easy it is to use. It can't be learned in 15-20 minutes so I eliminated it as an option (more time wasted)
  - Also requires an Influx license would make it hard to you to reproduce if you don't have it
- Implement aggregation via DB : Relational
    - See repository package (@Repository = RecordRepository )
    - I ran out of time trying to create SQL queries
- Implement aggregation via JSON query language (e.g. JsonPath or JSONNat)
  - I spent some time refreshing my memory on JSON query language but it was clear that it would take me even longer to implement querying and aggregation because I was rusty on the sytax