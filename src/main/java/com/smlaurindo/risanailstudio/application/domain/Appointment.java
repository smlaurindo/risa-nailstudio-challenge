package com.smlaurindo.risanailstudio.application.domain;

import com.smlaurindo.risanailstudio.shared.exception.BusinessRuleException;
import com.smlaurindo.risanailstudio.shared.exception.ErrorCode;

import java.time.*;
import java.util.UUID;

import static java.time.Instant.now;

public class Appointment {
    private final String id;
    private final String customerId;
    private final String serviceId;
    private final AppointmentSlot slot;
    private AppointmentStatus status;
    private Instant confirmedAt;
    private Instant cancelledAt;
    private final Instant createdAt;

    private Appointment(String customerId, String serviceId, AppointmentSlot slot) {
        this.id = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.slot = slot;
        this.createdAt = now();
        this.status = AppointmentStatus.PENDING;
    }

    public Appointment(
            String id,
            String customerId,
            String serviceId,
            AppointmentSlot slot,
            AppointmentStatus status,
            Instant confirmedAt,
            Instant cancelledAt,
            Instant createdAt
    ) {
        this.id = id;
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.slot = slot;
        this.status = status;
        this.confirmedAt = confirmedAt;
        this.cancelledAt = cancelledAt;
        this.createdAt = createdAt;
    }

    public static Appointment schedule(
            String customerId,
            String serviceId,
            AppointmentSlot slot
    ) {
        return new Appointment(customerId, serviceId, slot);
    }

    public void confirm() {
        switch (status) {
            case CONFIRMED -> throw new BusinessRuleException(ErrorCode.APPOINTMENT_ALREADY_CONFIRMED);
            case CANCELLED -> throw new BusinessRuleException(ErrorCode.APPOINTMENT_ALREADY_CANCELLED);
        }

        if (slot.isInThePast()) {
            throw new BusinessRuleException(ErrorCode.APPOINTMENT_IN_THE_PAST);
        }

        this.status = AppointmentStatus.CONFIRMED;
        this.confirmedAt = now();
    }

    public void cancel() {
        if (status == AppointmentStatus.CANCELLED) {
            throw new BusinessRuleException(ErrorCode.APPOINTMENT_ALREADY_CANCELLED);
        }

        this.status = AppointmentStatus.CANCELLED;
        this.cancelledAt = now();
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public AppointmentSlot getSlot() {
        return slot;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public Instant getConfirmedAt() {
        return confirmedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
