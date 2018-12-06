package com.mycompany.crm.service.api;

import java.time.Instant;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.mycompany.crm.entity.AppointmentStatus;

import lombok.Data;

@Data
public class AppointmentDto extends VersionDto {

	@FutureOrPresent
	private Instant startTime;
	
	@Future
	private Instant endTime;
	
	private AppointmentStatus appointmentStatus;
	
	@NotNull
	@Positive
	private Long customerId;

}
