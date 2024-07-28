package com.tumuhairwe.assessment.api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.*;

@Data
@Entity
@Table(schema = "public", name ="call_record")
public class JPACallRecord {

    @EmbeddedId
    private JPACallRecordId id;

    @Column(name = "start_timestamp", columnDefinition= "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDateTime startTimestamp;

    @Column(name = "end_timestamp", columnDefinition= "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDateTime endTimestamp;

    public Duration calcDuration(){
        return Duration.between(startTimestamp, endTimestamp);
    }

    public Integer getCustomerId() {
        return id.getCustomerId();
    }

    public boolean isActiveOn(LocalDate date){
        return getStartDate().isBefore(date) || getEndDate().isBefore(date);
    }

    public LocalDate getStartDate(){
        return startTimestamp.toLocalDate();
    }
    public LocalDate getEndDate(){
        return endTimestamp.toLocalDate();
    }
}
