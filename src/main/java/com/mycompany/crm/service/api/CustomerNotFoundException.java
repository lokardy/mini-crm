package com.mycompany.crm.service.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)

public class CustomerNotFoundException extends GenericBusinessServiceException {

public CustomerNotFoundException( Long customerId) {
	super("Unable to find customer with customer Id "+ customerId);

}


	
}
