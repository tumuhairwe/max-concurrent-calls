package com.tumuhairwe.assessment.api.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class JPACallRecordId implements Serializable {

    private int customerId;
    private String callId;
}
