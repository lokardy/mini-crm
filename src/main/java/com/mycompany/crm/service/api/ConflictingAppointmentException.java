package com.mycompany.crm.service.api;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(code = HttpStatus.CONFLICT)
public class ConflictingAppointmentException  extends GenericBusinessServiceException {

	private Long conflcitingAppointmentId;
	private Instant conflcitingAppointmentStartTime;
	private Instant conflcitingAppointmentEndTime;
	private Instant requestedStartTime;
	public ConflictingAppointmentException( Long conflcitingAppointmentId,
			Instant conflcitingAppointmentStartTime, Instant conflcitingAppointmentEndTime,
			Instant requestedStartTime) {
		super("Conflicting appointment for the start time " + requestedStartTime  + 
				" Appointment with Id " + conflcitingAppointmentId 
				 + "with start time " + conflcitingAppointmentStartTime
				 + " and end time " + conflcitingAppointmentEndTime 
				 + "already active");
		
		this.conflcitingAppointmentId = conflcitingAppointmentId;
		this.conflcitingAppointmentStartTime = conflcitingAppointmentStartTime;
		this.conflcitingAppointmentEndTime = conflcitingAppointmentEndTime;
		this.requestedStartTime = requestedStartTime;
	}
	
	

}
