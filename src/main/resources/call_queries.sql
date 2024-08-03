WITH CallIntervals AS (
    SELECT
        start_timestamp AS timestamp,
        1 AS event_type
    FROM public.call_records
    UNION ALL
    SELECT
        end_timestamp AS timestamp,
        -1 AS event_type
    FROM public."Calls"
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
             customer_id,
             COUNT(call_id) AS call_count
         FROM public."Calls"
         GROUP BY customer_id
         ORDER BY call_count DESC
         LIMIT 1
     ),
     PeakCallIdentifiers AS (
         SELECT DISTINCT c.call_id
         FROM public."Calls" c
         WHERE EXISTS (
                       SELECT 1
                       FROM PeakTimestamps p
                       WHERE c.start_timestamp <= p.timestamp AND c.end_timestamp >= p.timestamp
                   )
     )
SELECT
    mdc.call_date AS peak_date,
    mdc.max_concurrent_calls AS max_concurrent_calls,
    mfc.customer_id AS top_customer_id,
    pt.timestamp AS peak_timestamp,
    pki.call_id AS call_id_during_peak
FROM MaxDailyConcurrent mdc
         CROSS JOIN MostFrequentCustomer mfc
         CROSS JOIN PeakTimestamps pt
         LEFT JOIN PeakCallIdentifiers pki
                   ON TRUE
WHERE mdc.call_date = DATE(pt.timestamp);
