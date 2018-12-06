package com.mycompany.minicrm.service.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.mycompany.crm.entity.Appointment;
import com.mycompany.crm.entity.AppointmentStatus;
import com.mycompany.crm.entity.Customer;
import com.mycompany.crm.repository.AppointmentRepository;
import com.mycompany.crm.repository.CustomerRepository;
import com.mycompany.crm.service.api.ActiveAppointmentsExistsException;
import com.mycompany.crm.service.api.AppointmentDto;
import com.mycompany.crm.service.api.AppointmentNotFoundException;
import com.mycompany.crm.service.api.ConflictingAppointmentException;
import com.mycompany.crm.service.api.CustomerNotFoundException;
import com.mycompany.crm.service.api.IdentityAppointmentDto;
import com.mycompany.crm.service.api.IdentityCustomerDto;
import com.mycompany.crm.service.api.PhoneNumberAlreadyExistsException;
import com.mycompany.crm.service.api.UpdateAppointmentStatusDto;
import com.mycompany.crm.service.impl.CustomerMapper;
import com.mycompany.crm.service.impl.CustomerService;


@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceUnitTest {

	@InjectMocks
	private CustomerService customerService;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private AppointmentRepository appointmentRepository;

	private Instant startTime = Instant.now();
	private Instant endTime = Instant.now().plus(10, ChronoUnit.MINUTES);

	
	@Test(expected = PhoneNumberAlreadyExistsException.class)
	public void createCustomerShouldThrowPhoneNumberAlreadyExistsExceptionInCaseOfDuplicateMobileNumber() {

		try {
			Customer customer = new Customer();

			customer.setCustomerId(1L);
			customer.setCustomerName("Lokesh");
			customer.setAddress("test");
			customer.setAge(20);
			customer.setPhoneNumber(9008407323L);
			
			when(customerRepository.findByPhoneNumber(any())).thenReturn(Optional.ofNullable(customer));

			 customerService
					.createCustomer(CustomerMapper.AS_CUSTOMER_DTO.apply(customer));
		} catch (PhoneNumberAlreadyExistsException ex) {
			assertThat(ex.getPhoneNumber()).isEqualByComparingTo(9008407323L);

			throw ex;
		}

	}
	
	@Test(expected = PhoneNumberAlreadyExistsException.class)
	public void updateCustomerShouldThrowPhoneNumberAlreadyExistsExceptionInCaseOfDuplicateMobileNumber() {

		try {
			Customer customer = new Customer();

			customer.setCustomerId(1L);
			customer.setCustomerName("Lokesh");
			customer.setAddress("test");
			customer.setAge(20);
			customer.setPhoneNumber(9008407323L);
			
			when(customerRepository.findByPhoneNumber(any())).thenReturn(Optional.ofNullable(customer));

			 customerService
					.updateCustomer(CustomerMapper.AS_CUSTOMER_IDENTITY_DTO.apply(customer));
		} catch (PhoneNumberAlreadyExistsException ex) {
			assertThat(ex.getPhoneNumber()).isEqualByComparingTo(9008407323L);

			throw ex;
		}

	}
	@Test
	public void shouldInsertCustomerForValidData() {

		Customer customer = new Customer();

		customer.setCustomerId(1L);
		customer.setCustomerName("Lokesh");
		customer.setAddress("test");
		customer.setAge(20);

		when(customerRepository.save(any())).thenReturn(customer);

		IdentityCustomerDto createdCustomerDto = customerService
				.createCustomer(CustomerMapper.AS_CUSTOMER_DTO.apply(customer));

		assertThat(CustomerMapper.AS_CUSTOMER_IDENTITY_DTO.apply(customer))
				.isEqualToComparingFieldByFieldRecursively(createdCustomerDto);

	}

	@Test
	public void shouldUpdateCustomerForValidData() {

		Customer customer = new Customer();

		customer.setCustomerId(1L);
		customer.setCustomerName("Lokesh");
		customer.setAddress("test");
		customer.setAge(20);

		when(customerRepository.save(any())).thenReturn(customer);

		IdentityCustomerDto createdCustomerDto = customerService
				.updateCustomer(CustomerMapper.AS_CUSTOMER_IDENTITY_DTO.apply(customer));

		verify(customerRepository).save(customer);
		assertThat(CustomerMapper.AS_CUSTOMER_IDENTITY_DTO.apply(customer))
				.isEqualToComparingFieldByFieldRecursively(createdCustomerDto);

	}

	@Test
	public void shouldFindCustomerForValidCustomerId() {

		Customer customer = new Customer();

		customer.setCustomerId(1L);
		customer.setCustomerName("Lokesh");
		customer.setAddress("test");
		customer.setAge(20);

		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

		IdentityCustomerDto createdCustomerDto = customerService.findCustomer(1L);

		assertThat(CustomerMapper.AS_CUSTOMER_IDENTITY_DTO.apply(customer))
				.isEqualToComparingFieldByFieldRecursively(createdCustomerDto);

	}

	@Test(expected = CustomerNotFoundException.class)
	public void findCustomerShouldThrowCustomerNotFoundExceptionInCaseOfInvalidCustomerId() {

		try {
			when(customerRepository.findById(2L)).thenReturn(Optional.empty());

			customerService.findCustomer(2L);
		} catch (CustomerNotFoundException ex) {
			assertThat(ex.getMessage()).contains("2");

			throw ex;
		}

	}

	@Test(expected = CustomerNotFoundException.class)
	public void deleteCustomerShouldThrowCustomerNotFoundExceptionInCaseOfInvalidCustomerId() {

		try {
			when(customerService.findCustomer(2L)).thenThrow(new CustomerNotFoundException(2L));

			customerService.deleteCustomer(2L);
		} catch (CustomerNotFoundException ex) {
			assertThat(ex.getMessage()).contains("2");

			throw ex;
		}

	}

	@Test
	public void shouldDeleteCustomerForValidCustomerId() {

		Customer customer = new Customer();

		customer.setCustomerId(1L);
		customer.setCustomerName("Lokesh");
		customer.setAddress("test");
		customer.setAge(20);

		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

		IdentityCustomerDto createdCustomerDto = customerService.deleteCustomer(1L);

		assertThat(CustomerMapper.AS_CUSTOMER_IDENTITY_DTO.apply(customer))
				.isEqualToComparingFieldByFieldRecursively(createdCustomerDto);

	}

	@Test
	public void shouldInsertAppointmentForValidDataAndCustomer() {

		Customer customer = new Customer();

		customer.setCustomerId(1L);
		customer.setCustomerName("Lokesh");
		customer.setAddress("test");
		customer.setAge(20);

		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

		when(appointmentRepository.findByStartTimeGreaterThanEqualAndEndTimeLessThanEqualAndAppointmentStatus(any(), any(), any()))
				.thenReturn(Optional.empty());

		Appointment appointment = new Appointment();

		appointment.setAppointmentStatus(AppointmentStatus.ACTIVE);
		appointment.setCustomer(customer);
		appointment.setStartTime(startTime);
		appointment.setEndTime(endTime);

		when(appointmentRepository.save(any())).thenReturn(appointment);

		AppointmentDto appointmentDto = new AppointmentDto();

		appointmentDto.setAppointmentStatus(AppointmentStatus.ACTIVE);
		appointmentDto.setCustomerId(1L);
		appointmentDto.setStartTime(startTime);
		appointmentDto.setEndTime(endTime);

		IdentityAppointmentDto createdAppointmentDto = customerService.createAppointment(appointmentDto);

		assertThat(appointmentDto).isEqualToIgnoringGivenFields(createdAppointmentDto);

	}

	@Test(expected = ConflictingAppointmentException.class)
	public void shouldThrowConflictingAppointmentExceptionInCaseOfExistingAppointmentAtTheSameTime() {

		try {
			Customer customer = new Customer();

			customer.setCustomerId(1L);
			customer.setCustomerName("Lokesh");
			customer.setAddress("test");
			customer.setAge(20);

			when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

			Appointment appointment = new Appointment();

			appointment.setAppointmentId(1L);
			appointment.setAppointmentStatus(AppointmentStatus.ACTIVE);
			appointment.setCustomer(customer);
			appointment.setStartTime(startTime);
			appointment.setEndTime(endTime);

			when(appointmentRepository.findByStartTimeGreaterThanEqualAndEndTimeLessThanEqualAndAppointmentStatus(any(), any(), any()))
					.thenReturn(Optional.of(appointment));

			AppointmentDto appointmentDto = new AppointmentDto();

			appointmentDto.setAppointmentStatus(AppointmentStatus.ACTIVE);
			appointmentDto.setCustomerId(1L);
			appointmentDto.setStartTime(startTime);
			appointmentDto.setEndTime(endTime);

			customerService.createAppointment(appointmentDto);
		} catch (ConflictingAppointmentException ex) {
			assertThat(ex.getConflcitingAppointmentId()).isEqualTo(1L);
			assertThat(ex.getConflcitingAppointmentStartTime()).isSameAs(startTime);
			assertThat(ex.getConflcitingAppointmentEndTime()).isSameAs(endTime);
			assertThat(ex.getRequestedStartTime()).isSameAs(startTime);

			throw ex;

		}
	}

	@Test(expected = CustomerNotFoundException.class)
	public void shouldThrowCustomerNotFoundExceptionInCaseOfWrongCustomerId() {

		try {
			Customer customer = new Customer();

			customer.setCustomerId(1L);
			customer.setCustomerName("Lokesh");
			customer.setAddress("test");
			customer.setAge(20);

			when(customerRepository.findById(1L)).thenReturn(Optional.empty());

			AppointmentDto appointmentDto = new AppointmentDto();

			appointmentDto.setAppointmentStatus(AppointmentStatus.ACTIVE);
			appointmentDto.setCustomerId(1L);
			appointmentDto.setStartTime(startTime);
			appointmentDto.setEndTime(endTime);

			customerService.createAppointment(appointmentDto);
		} catch (CustomerNotFoundException ex) {
			assertThat(ex.getMessage()).contains("1");
			throw ex;

		}

	}

	@Test
	public void shouldUpdateAppointmentStatus() {

		Customer customer = new Customer();

		customer.setCustomerId(1L);
		customer.setCustomerName("Lokesh");
		customer.setAddress("test");
		customer.setAge(20);
		Appointment appointment = new Appointment();

		appointment.setAppointmentStatus(AppointmentStatus.ACTIVE);
		appointment.setCustomer(customer);
		appointment.setStartTime(startTime);
		appointment.setEndTime(endTime);

		when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

		appointment.setAppointmentStatus(AppointmentStatus.INACTIVE);

		when(appointmentRepository.save(any())).thenReturn(appointment);

		UpdateAppointmentStatusDto updateAppointmentStatusDto = new UpdateAppointmentStatusDto(1L,
				AppointmentStatus.INACTIVE);

		IdentityAppointmentDto updatedAppointmentDto = customerService
				.updateAppointmentStatus(updateAppointmentStatusDto);

	 Assert.assertEquals(AppointmentStatus.INACTIVE, updatedAppointmentDto.getAppointmentStatus());
	}
	
	@Test(expected = AppointmentNotFoundException.class)
	public void shouldThrowAppointmentNotFoundExceptionInCaseofWrongAppointmentId() {

		try {
		when(appointmentRepository.findById(1L)).thenThrow(new AppointmentNotFoundException(1L));


		UpdateAppointmentStatusDto updateAppointmentStatusDto = new UpdateAppointmentStatusDto(1L,
				AppointmentStatus.INACTIVE);

		IdentityAppointmentDto updatedAppointmentDto = customerService
				.updateAppointmentStatus(updateAppointmentStatusDto);

	 Assert.assertEquals(AppointmentStatus.INACTIVE, updatedAppointmentDto.getAppointmentStatus());
		} catch(AppointmentNotFoundException ex) {
			assertThat(ex.getAppointmentId()).isEqualByComparingTo(1L);
			
			throw ex;
		}
	}
	
	
	@Test(expected = ActiveAppointmentsExistsException.class)
	public void deleteCustomerShouldThrowActiveAppointmentsExistsExceptionInCaseOfActiveAppointments() {

		try {
			Customer customer = new Customer();

			customer.setCustomerId(1L);
			customer.setCustomerName("Lokesh");
			customer.setAddress("test");
			customer.setAge(20);
			
			Appointment appointment = new Appointment();

			appointment.setAppointmentId(1L);
			appointment.setAppointmentStatus(AppointmentStatus.ACTIVE);
			appointment.setCustomer(customer);
			appointment.setStartTime(startTime);
			appointment.setEndTime(endTime);

			
			when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
			
			when(appointmentRepository.findByAppointmentStatusAndCustomerId(any(), any())).thenReturn(Collections.singletonList(appointment));

			customerService.deleteCustomer(1L);
		} catch (ActiveAppointmentsExistsException ex) {
			assertThat(ex.getCustomerId()).isEqualByComparingTo(1L);

			throw ex;
		}

	}
}
