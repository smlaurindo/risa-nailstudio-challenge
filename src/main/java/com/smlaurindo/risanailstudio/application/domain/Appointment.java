package com.smlaurindo.risanailstudio.application.domain;

import java.time.*;

import static java.time.Instant.now;

public class Appointment {
    private String id;
    private String customerId;
    private String serviceId;
    private AppointmentSlot slot;
    private AppointmentStatus status;
    private Instant acceptedAt;
    private Instant cancelledAt;
    private final Instant createdAt;

    private Appointment(String customerId, String serviceId, AppointmentSlot slot) {
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
            Instant acceptedAt,
            Instant cancelledAt,
            Instant createdAt
    ) {
        this.id = id;
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.slot = slot;
        this.status = status;
        this.acceptedAt = acceptedAt;
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

//    public void accept() {
//        if (status != AppointmentStatus.PENDING) {
//            throw new DomainException(
//                    "Only pending appointments can be accepted"
//            );
//        }
//
//        this.status = AppointmentStatus.ACCEPTED;
//        this.acceptedAt = now();
//    }
//
//    public void cancel() {
//        if (status == AppointmentStatus.CANCELLED) {
//            throw new DomainException(
//                    "Appointment is already cancelled"
//            );
//        }
//
//        this.status = AppointmentStatus.CANCELLED;
//        this.cancelledAt = now();
//    }

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

    public Instant getAcceptedAt() {
        return acceptedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
