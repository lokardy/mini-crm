package com.mycompany.crm.service.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(code = HttpStatus.CONFLICT)
public class ActiveAppointmentsExistsException extends GenericBusinessServiceException {

	public ActiveAppointmentsExistsException(Long customerId) {
		super("Äctive appointments " + "exists for the given customer Id " + customerId);
		this.customerId = customerId;
	}

	private Long customerId;
}
