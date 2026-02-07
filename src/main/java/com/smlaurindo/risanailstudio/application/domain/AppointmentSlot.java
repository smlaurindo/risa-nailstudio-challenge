package com.smlaurindo.risanailstudio.application.domain;

import com.smlaurindo.risanailstudio.application.exception.BusinessRuleException;
import com.smlaurindo.risanailstudio.application.exception.ErrorCode;

import java.time.*;

public record AppointmentSlot(
        Instant startsAt,
        Instant endsAt
) {
    public AppointmentSlot {
        if (endsAt.isBefore(startsAt) || endsAt.equals(startsAt)) {
            throw new BusinessRuleException(ErrorCode.APPOINTMENT_SLOT_INVALID);
        }
    }

    public static AppointmentSlot from(Instant scheduledAt, int durationMinutes) {
        var endsAt = scheduledAt.plus(Duration.ofMinutes(durationMinutes));
        return new AppointmentSlot(scheduledAt, endsAt);
    }

    public Duration getDuration() {
        return Duration.between(startsAt, endsAt);
    }

    public boolean isInThePast() {
        return startsAt.isBefore(Instant.now());
    }
}

