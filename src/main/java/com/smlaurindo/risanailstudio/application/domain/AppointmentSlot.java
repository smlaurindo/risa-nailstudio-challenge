package com.smlaurindo.risanailstudio.application.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AppointmentSlot(
        LocalDateTime startsAt,
        LocalDateTime endsAt
) {
    public AppointmentSlot {
        if (endsAt.isBefore(startsAt) || endsAt.isEqual(startsAt)) {
            throw new IllegalArgumentException(
                    "AppointmentSlot endsAt must be after startsAt"
            );
        }
    }

    public static AppointmentSlot from(LocalTime localTime, LocalDate localDate, int durationMinutes) {
        var startsAt = LocalDateTime.of(localDate, localTime);
        var endsAt = startsAt.plusMinutes(durationMinutes);

        return new AppointmentSlot(startsAt, endsAt);
    }

    public boolean overlaps(AppointmentSlot other) {
        return startsAt.isBefore(other.endsAt)
                && endsAt.isAfter(other.startsAt);
    }

    public Duration getDuration() {
        return Duration.between(startsAt, endsAt);
    }
}

