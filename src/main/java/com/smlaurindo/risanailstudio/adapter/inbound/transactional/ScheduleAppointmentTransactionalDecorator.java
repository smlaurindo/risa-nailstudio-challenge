package com.smlaurindo.risanailstudio.adapter.inbound.transactional;

import com.smlaurindo.risanailstudio.application.usecase.ScheduleAppointment;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
public class ScheduleAppointmentTransactionalDecorator implements ScheduleAppointment {

    private final ScheduleAppointment delegate;

    @Override
    @Transactional
    public ScheduleAppointmentOutput scheduleAppointment(ScheduleAppointmentInput input) {
        return delegate.scheduleAppointment(input);
    }
}
