WITH CallIntervals AS (
    SELECT
        "startTimestamp" AS timestamp,
        1 AS event_type
    FROM leetcode."Calls"
    UNION ALL
    SELECT
        "endTimestamp" AS timestamp,
        -1 AS event_type
    FROM leetcode."Calls"
),
     DailyConcurrentCounts AS (
         SELECT
             DATE(timestamp) AS call_date,
             SUM(event_type) OVER (PARTITION BY DATE(timestamp) ORDER BY timestamp ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS concurrent_calls
         FROM CallIntervals
     ),
     MaxDailyConcurrent AS (
         SELECT
             call_date,
             MAX(concurrent_calls) AS max_concurrent_calls
         FROM DailyConcurrentCounts
         GROUP BY call_date
     ),
     CallDetails AS (
         SELECT
             timestamp,
             SUM(event_type) OVER (ORDER BY timestamp ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS concurrent_calls
         FROM CallIntervals
     ),
     MaxConcurrent AS (
         SELECT
             MAX(concurrent_calls) AS peak_concurrent_calls
         FROM CallDetails
     ),
     PeakTimestamps AS (
         SELECT
             timestamp,
             concurrent_calls
         FROM CallDetails
         WHERE concurrent_calls = (SELECT peak_concurrent_calls FROM MaxConcurrent)
     ),
     MostFrequentCustomer AS (
         SELECT
             "customerId",
             COUNT("callerId") AS call_count
         FROM leetcode."Calls"
         GROUP BY "customerId"
         ORDER BY call_count DESC
         LIMIT 1
     ),
     PeakCallIdentifiers AS (
         SELECT DISTINCT c."callerId"
         FROM leetcode."Calls" c
         WHERE EXISTS (
                       SELECT 1
                       FROM PeakTimestamps p
                       WHERE c."startTimestamp" <= p.timestamp AND c."endTimestamp" >= p.timestamp
                   )
     )
SELECT
    mdc.call_date AS peak_date,
    mdc.max_concurrent_calls AS max_concurrent_calls,
    mfc."customerId" AS top_customer_id,
    pt.timestamp AS peak_timestamp,
    pki."callerId" AS call_id_during_peak
FROM MaxDailyConcurrent mdc
         CROSS JOIN MostFrequentCustomer mfc
         CROSS JOIN PeakTimestamps pt
         LEFT JOIN PeakCallIdentifiers pki
                   ON TRUE
WHERE mdc.call_date = DATE(pt.timestamp);