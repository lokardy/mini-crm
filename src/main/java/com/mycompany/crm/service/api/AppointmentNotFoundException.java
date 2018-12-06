package com.mycompany.crm.service.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AppointmentNotFoundException extends GenericBusinessServiceException {

	private Long appointmentId;
	
	public AppointmentNotFoundException(Long appointmentId) {
		super("Äppointment with Id " + appointmentId + " not found");
		
		this.appointmentId = appointmentId;
		
	}
	

}
