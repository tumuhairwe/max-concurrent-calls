package com.tumuhairwe.assessment.repository;

import com.tumuhairwe.assessment.api.model.JPACallRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface RecordRepository extends JpaRepository<JPACallRecord, Long> {

    @Query("SELECT " +
            "count(*) as count_by_date, customer_id, call_id, start_timestamp, end_timestamp " +
            "FROM call_record " +
            "GROUP BY CAST(start_timestamp AS DATE), customer_id, call_id")
    List<Map<String, Object>> getCount();

    @Query("SELECT count(*) as max_by_date " +
            "FROM call_record " +
            "WHERE start_timestamp >= :startTimestamp " +
            "AND end_timestamp <= :endTimestamp " +
            "AND customer_id = :customerId " +
            "GROUP BY CAST(start_timestamp AS DATE)" +
            "ORDER BY max_by_date " +
            "LIMIT 1")
    Integer getPeakCountByCustomerAndDate(@Param("customerId") Integer customerId,
                                  @Param("startTimestamp")LocalDateTime startTimestamp,
                                @Param("endTimestamp")LocalDateTime endTimestamp);

    Integer getPeakDateByCustomerAndDate(@Param("customerId") Integer customerId,
                                          @Param("startTimestamp")LocalDateTime startTimestamp,
                                          @Param("endTimestamp")LocalDateTime endTimestamp);

    @Query("SELECT c.id.customerId, CAST(c.startTimestamp AS LocalDate) FROM JPACallRecord c GROUP BY c.id.customerId")
    Map<Integer, LocalDate> findDistinctDates();

    @Query("SELECT count(c.id.callId) " +
            "FROM JPACallRecord c " +
            "WHERE j.id.customerId = :customerId " +
            "AND CAST(c.startTimestamp AS LocalDate) = :callDate")
    Integer findMaxCallsByDateAndCustomer(@Param("callDate") LocalDate callDate, @Param("customerId") Integer customerId);
}
