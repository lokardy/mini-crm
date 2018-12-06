package com.mycompany.crm.service.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class IdentityCustomerDto extends CustomerDto {

	@JsonIgnore
	private Long customerId;
}
