package com.mycompany.crm.controller;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.crm.service.api.AppointmentDto;
import com.mycompany.crm.service.api.CustomerDto;
import com.mycompany.crm.service.api.CustomerServiceInterface;
import com.mycompany.crm.service.api.IdentityAppointmentDto;
import com.mycompany.crm.service.api.IdentityCustomerDto;
import com.mycompany.crm.service.api.UpdateAppointmentStatusDto;
import com.mycompany.crm.service.impl.CustomerMapper;

@RestController

public class CustomerController {
	
	@Autowired
	private CustomerServiceInterface customerService;
	
	@PostMapping("/customers")
	@ResponseBody
	public IdentityCustomerDto createCustomer(@Valid @RequestBody CustomerDto customerDto) {
		
		return customerService.createCustomer(customerDto);
	}
	
	@PutMapping("/customers/{customerId}")
	@ResponseBody
	public IdentityCustomerDto updateCustomer(@PathParam("customerId") Long customerId, @Valid @RequestBody CustomerDto customerDto) {
		IdentityCustomerDto identityCustomerDto = CustomerMapper.TO_CUSTOMER_IDENTITY_DTO.apply(customerDto, customerId);
		
		return customerService.updateCustomer(identityCustomerDto);
	}
	
	@GetMapping("/customers/{customerId}")
	@ResponseBody
	public IdentityCustomerDto getCustomer(@PathVariable("customerId") Long customerId) {
		
		return customerService.findCustomer(customerId);
	}
	
	@DeleteMapping("/customers/{customerId}")
	@ResponseBody
	public IdentityCustomerDto deleteCustomer(@PathVariable("customerId") Long customerId) {
		
		return customerService.deleteCustomer(customerId);
	}
	
	@PostMapping("/customers/{customerId}/appointments")
	@ResponseBody
	public IdentityAppointmentDto createAppointment(@PathVariable("customerId") Long customerId, @Valid @RequestBody AppointmentDto appointmentDto) {
		
		//TODO : Need to find better way to solve this. Take out customer Id from the dto?
		appointmentDto.setCustomerId(customerId);

		return customerService.createAppointment(appointmentDto);
	}
	
	@PatchMapping("/customers/{customerId}/appointments/{appointmentId}")
	@ResponseBody
	public IdentityAppointmentDto updateAppointmentStatus(@PathVariable("customerId") Long customerId, @PathVariable("appointmentId") Long appointmentId,  @Valid @RequestBody UpdateAppointmentStatusDto updateAppointmentStatusDto) {

// TODO: Need to check if appointment belong to given customer.
		updateAppointmentStatusDto.setAppointmentId(appointmentId);
		
		return customerService.updateAppointmentStatus(updateAppointmentStatusDto);
	}

}
