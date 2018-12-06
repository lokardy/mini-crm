package com.mycompany.crm.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.crm.entity.Appointment;
import com.mycompany.crm.entity.AppointmentStatus;
import com.mycompany.crm.entity.Customer;
import com.mycompany.crm.repository.AppointmentRepository;
import com.mycompany.crm.repository.CustomerRepository;
import com.mycompany.crm.service.api.ActiveAppointmentsExistsException;
import com.mycompany.crm.service.api.AppointmentDto;
import com.mycompany.crm.service.api.AppointmentNotFoundException;
import com.mycompany.crm.service.api.ConflictingAppointmentException;
import com.mycompany.crm.service.api.CustomerDto;
import com.mycompany.crm.service.api.CustomerNotFoundException;
import com.mycompany.crm.service.api.CustomerServiceInterface;
import com.mycompany.crm.service.api.IdentityAppointmentDto;
import com.mycompany.crm.service.api.IdentityCustomerDto;
import com.mycompany.crm.service.api.PhoneNumberAlreadyExistsException;
import com.mycompany.crm.service.api.UpdateAppointmentStatusDto;

@Service
@Transactional

public class CustomerService implements CustomerServiceInterface {
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Override
	public IdentityCustomerDto createCustomer(CustomerDto customerDto) {

		validateCustomer(customerDto);

		Customer customer = CustomerMapper.AS_CUSTOMER.apply(customerDto);

		Customer returnCustomer = customerRepository.save(customer);

		return CustomerMapper.AS_CUSTOMER_IDENTITY_DTO.apply(returnCustomer);
	}

	@Override
	public IdentityCustomerDto updateCustomer(IdentityCustomerDto customerDto) {
		validateCustomer(customerDto);

		Customer customer = CustomerMapper.AS_CUSTOMER.apply(customerDto);

		Customer returnCustomer = customerRepository.save(customer);

		return CustomerMapper.AS_CUSTOMER_IDENTITY_DTO.apply(returnCustomer);
	}

	@Override
	public IdentityCustomerDto findCustomer(Long customerId) {
		return customerRepository.findById(customerId).map(customer -> {
			return CustomerMapper.AS_CUSTOMER_IDENTITY_DTO.apply(customer);

		}).orElseThrow(() -> new CustomerNotFoundException(customerId));

	}

	@Override

	public IdentityCustomerDto deleteCustomer(Long customerId) {
		IdentityCustomerDto identityCustomerDto = findCustomer(customerId);


		if (areThereAnyActiveAppointments(customerId)) {
			throw new ActiveAppointmentsExistsException(customerId);
		}

		customerRepository.deleteById(customerId);

		return identityCustomerDto;
	}

	@Override
	public IdentityAppointmentDto createAppointment(AppointmentDto appointmentDto) {
		validateCustomer(appointmentDto.getCustomerId());

		appointmentRepository.findByStartTimeGreaterThanEqualAndEndTimeLessThanEqualAndAppointmentStatus(appointmentDto.getStartTime(),
				appointmentDto.getEndTime(), AppointmentStatus.ACTIVE).ifPresent(conflictingAppointment -> {
					if (conflictingAppointment.getAppointmentStatus() == AppointmentStatus.ACTIVE) {
						throw new ConflictingAppointmentException(conflictingAppointment.getAppointmentId(),
								conflictingAppointment.getStartTime(), conflictingAppointment.getEndTime(),
								appointmentDto.getStartTime());
					}

				});

		Appointment appointment = AppointmentMapper.AS_APPOINTMENT.apply(appointmentDto, customerRepository.findById(appointmentDto.getCustomerId()).get());

		Appointment returnAppointment = appointmentRepository.save(appointment);

		return AppointmentMapper.AS_IDENTITY_APPOINTMENT_DTO.apply(returnAppointment);
	}

	@Override
	public IdentityAppointmentDto updateAppointmentStatus(UpdateAppointmentStatusDto updateAppointmentStatusDto) {
		//TODO: Add validation to check if current appointment in progress and about to start and the status is same as that of given status.

		return appointmentRepository.findById(updateAppointmentStatusDto.getAppointmentId()).map(appointment -> {

			appointment.setAppointmentStatus(updateAppointmentStatusDto.getAppointmentStatus());

			return AppointmentMapper.AS_IDENTITY_APPOINTMENT_DTO.apply(appointmentRepository.save(appointment));
		}).orElseThrow(() -> new AppointmentNotFoundException(updateAppointmentStatusDto.getAppointmentId()));

	}

	private void validateCustomer(CustomerDto customerDto) {
		customerRepository.findByPhoneNumber(customerDto.getPhoneNumber()).ifPresent(customer -> {
			throw new PhoneNumberAlreadyExistsException(customerDto.getPhoneNumber());
		});
	}

	private boolean areThereAnyActiveAppointments(Long  customerId) {
		List<Appointment> appointments = appointmentRepository.findByAppointmentStatusAndCustomerId(AppointmentStatus.ACTIVE, customerId);
		
		return !appointments.isEmpty();
	}

	private void validateCustomer(Long customerId) {
		findCustomer(customerId);
	}

}
