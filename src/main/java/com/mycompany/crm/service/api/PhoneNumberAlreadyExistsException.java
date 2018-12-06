package com.mycompany.crm.service.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Data;

@Data
@ResponseStatus(code = HttpStatus.CONFLICT)
public class PhoneNumberAlreadyExistsException extends GenericBusinessServiceException {

	private Long phoneNumber;

	public PhoneNumberAlreadyExistsException(Long phoneNumber) {
		super("Customer with phone number " + phoneNumber + " already exists");
		this.phoneNumber = phoneNumber;
	}

}
