package com.mycompany.crm.service.api;

import com.mycompany.crm.entity.AppointmentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateAppointmentStatusDto {
	
	private Long appointmentId;
	
	private AppointmentStatus appointmentStatus;

}
