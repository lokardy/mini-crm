package com.mycompany.minicrm.service.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.crm.entity.AppointmentStatus;
import com.mycompany.crm.entity.Customer;
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
import com.mycompany.crm.service.impl.CustomerMapper;
import com.mycompany.minicrm.MiniCrmApplication;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = MiniCrmApplication.class)
@Transactional
public class CustomerServiceIntegrationTest {

	@Autowired
	private CustomerServiceInterface customerService;

	@Autowired
	private CustomerRepository customerRepository;

	private PodamFactory podamFactory = new PodamFactoryImpl();

	private IdentityCustomerDto createdCustomer;
	
	private Instant startTime = Instant.now();
	private Instant endTime = Instant.now().plus(10, ChronoUnit.MINUTES);
	
	
	private IdentityAppointmentDto createdAppointmentDto;
	
	private IdentityCustomerDto customerWithAppointments;
	
	

	@Before
	public void before() {
		Customer customer = new Customer();

		customer.setCustomerId(1L);
		customer.setCustomerName("Lokesh");
		customer.setAddress("test");
		customer.setAge(20);
		customer.setVersion(1L);
		customer.setPhoneNumber(9008407323L);

		createdCustomer = customerService.createCustomer(CustomerMapper.AS_CUSTOMER_DTO.apply(customer));
		
		 customer = new Customer();

		customer.setCustomerName("Lokesh");
		customer.setAddress("test");
		customer.setAge(20);
		customer.setVersion(1L);
		customer.setPhoneNumber(9008408323L);

		customerWithAppointments = customerService.createCustomer(CustomerMapper.AS_CUSTOMER_DTO.apply(customer));
		
		AppointmentDto appointmentDto = new AppointmentDto();

		appointmentDto.setAppointmentStatus(AppointmentStatus.ACTIVE);
		appointmentDto.setCustomerId(customerWithAppointments.getCustomerId());
		appointmentDto.setStartTime(startTime);
		appointmentDto.setEndTime(endTime);
		appointmentDto.setVersionId(1L);

		createdAppointmentDto = customerService.createAppointment(appointmentDto);

	}

	
	@After
	public void after() {

		UpdateAppointmentStatusDto updateAppointmentStatusDto = new UpdateAppointmentStatusDto(createdAppointmentDto.getAppointmentId(),
				AppointmentStatus.INACTIVE);

		customerService
				.updateAppointmentStatus(updateAppointmentStatusDto);


	}
	@Test(expected = PhoneNumberAlreadyExistsException.class)
	public void createCustomerShouldThrowPhoneNumberAlreadyExistsExceptionInCaseOfDuplicateMobileNumber() {

		try {

			customerService.createCustomer(createdCustomer);
		} catch (PhoneNumberAlreadyExistsException ex) {
			assertThat(ex.getPhoneNumber()).isEqualByComparingTo(9008407323L);

			throw ex;
		}

	}

	@Test(expected = PhoneNumberAlreadyExistsException.class)
	public void updateCustomerShouldThrowPhoneNumberAlreadyExistsExceptionInCaseOfDuplicateMobileNumber() {

		try {

			customerService.updateCustomer(createdCustomer);
		} catch (PhoneNumberAlreadyExistsException ex) {
			assertThat(ex.getPhoneNumber()).isEqualByComparingTo(9008407323L);

			throw ex;
		}

	}

	@Test
	public void shouldInsertCustomerForValidData() {

		CustomerDto customerDto = podamFactory.manufacturePojo(CustomerDto.class);

		IdentityCustomerDto createdCustomer = customerService.createCustomer(customerDto);

		assertThat(customerDto).isEqualToIgnoringGivenFields(createdCustomer, "customerId", "versionId");

	}

	@Test
	public void shouldUpdateCustomerForValidData() {

		createdCustomer.setCustomerName("Lokesh");
		createdCustomer.setPhoneNumber(9008409323L);

		IdentityCustomerDto returnedCustomer = customerService.updateCustomer(createdCustomer);

		assertThat(createdCustomer).isEqualToComparingFieldByFieldRecursively(returnedCustomer);

	}

	@Test
	public void shouldGetCustomerForValidCustomerId() {

		IdentityCustomerDto returnedCustomer = customerService.findCustomer(createdCustomer.getCustomerId());

		assertThat(createdCustomer).isEqualToComparingFieldByFieldRecursively(returnedCustomer);

	}

	@Test
	public void shouldDeleteCustomerForValidCustomerId() {
		customerService.deleteCustomer(createdCustomer.getCustomerId());

		assertThat(customerRepository.findById(createdCustomer.getCustomerId())).isNotPresent();

	}

	@Test(expected = CustomerNotFoundException.class)
	public void deleteCustomerShouldThrowCustomerNotFoundExceptionInCaseOfInvalidCustomerId() {

		try {

			customerService.deleteCustomer(-1L);
		} catch (CustomerNotFoundException ex) {
			assertThat(ex.getMessage()).contains("-1");

			throw ex;
		}

	}

	@Test(expected = CustomerNotFoundException.class)
	public void findCustomerShouldThrowCustomerNotFoundExceptionInCaseOfInvalidCustomerId() {

		try {

			customerService.findCustomer(-1L);
		} catch (CustomerNotFoundException ex) {
			assertThat(ex.getMessage()).contains("1");

			throw ex;
		}

	}
	
	
	@Test
	public void shouldInsertAppointmentForValidDataAndCustomer() {

		
		AppointmentDto appointmentDto = new AppointmentDto();

		appointmentDto.setAppointmentStatus(AppointmentStatus.ACTIVE);
		appointmentDto.setCustomerId(customerWithAppointments.getCustomerId());
		appointmentDto.setStartTime(startTime.plus(20, ChronoUnit.MINUTES));
		appointmentDto.setEndTime(endTime.plus(40, ChronoUnit.MINUTES));
		appointmentDto.setVersionId(1L);

		IdentityAppointmentDto createdAppointmentDto = customerService.createAppointment(appointmentDto);

		assertThat(appointmentDto).isEqualToIgnoringGivenFields(createdAppointmentDto);

	}

	@Test(expected = ConflictingAppointmentException.class)
	public void shouldThrowConflictingAppointmentExceptionInCaseOfExistingAppointmentAtTheSameTime() {

		try {
			
			AppointmentDto appointmentDto = new AppointmentDto();

			appointmentDto.setAppointmentStatus(AppointmentStatus.ACTIVE);
			appointmentDto.setCustomerId(customerWithAppointments.getCustomerId());
			appointmentDto.setStartTime(startTime);
			appointmentDto.setEndTime(endTime);
			appointmentDto.setVersionId(1L);

			customerService.createAppointment(appointmentDto);

			customerService.createAppointment(appointmentDto);
		} catch (ConflictingAppointmentException ex) {
			assertThat(ex.getConflcitingAppointmentId()).isEqualTo(createdAppointmentDto.getAppointmentId());
			assertThat(ex.getConflcitingAppointmentStartTime()).isSameAs(startTime);
			assertThat(ex.getConflcitingAppointmentEndTime()).isSameAs(endTime);
			assertThat(ex.getRequestedStartTime()).isSameAs(startTime);

			throw ex;

		}
	}

	@Test(expected = CustomerNotFoundException.class)
	public void shouldThrowCustomerNotFoundExceptionInCaseOfWrongCustomerId() {

		try {

			AppointmentDto appointmentDto = new AppointmentDto();

			appointmentDto.setAppointmentStatus(AppointmentStatus.ACTIVE);
			appointmentDto.setCustomerId(2L);
			appointmentDto.setStartTime(startTime);
			appointmentDto.setEndTime(endTime);

			customerService.createAppointment(appointmentDto);
		} catch (CustomerNotFoundException ex) {
			assertThat(ex.getMessage()).contains("2");
			throw ex;

		}

	}

	@Test
	public void shouldUpdateAppointmentStatus() {

		AppointmentDto appointmentDto = new AppointmentDto();

		appointmentDto.setAppointmentStatus(AppointmentStatus.ACTIVE);
		appointmentDto.setCustomerId(createdCustomer.getCustomerId());
		appointmentDto.setStartTime(startTime.plus(50, ChronoUnit.MINUTES));
		appointmentDto.setEndTime(endTime.plus(50, ChronoUnit.MINUTES));

		IdentityAppointmentDto identityAppointmentDto = customerService.createAppointment(appointmentDto);

		UpdateAppointmentStatusDto updateAppointmentStatusDto = new UpdateAppointmentStatusDto(identityAppointmentDto.getAppointmentId(),
				AppointmentStatus.INACTIVE);

		IdentityAppointmentDto updatedAppointmentDto = customerService
				.updateAppointmentStatus(updateAppointmentStatusDto);

	 Assert.assertEquals(AppointmentStatus.INACTIVE, updatedAppointmentDto.getAppointmentStatus());
	}
	
	@Test(expected = AppointmentNotFoundException.class)
	public void shouldThrowAppointmentNotFoundExceptionInCaseofWrongAppointmentId() {

		try {

		UpdateAppointmentStatusDto updateAppointmentStatusDto = new UpdateAppointmentStatusDto(100L,
				AppointmentStatus.INACTIVE);

		IdentityAppointmentDto updatedAppointmentDto = customerService
				.updateAppointmentStatus(updateAppointmentStatusDto);

	 Assert.assertEquals(AppointmentStatus.INACTIVE, updatedAppointmentDto.getAppointmentStatus());
		} catch(AppointmentNotFoundException ex) {
			assertThat(ex.getAppointmentId()).isEqualByComparingTo(100L);
			
			throw ex;
		}
	}
	
	
	@Test(expected = ActiveAppointmentsExistsException.class)
	public void deleteCustomerShouldThrowActiveAppointmentsExistsExceptionInCaseOfActiveAppointments() {

		try {

			customerService.deleteCustomer(customerWithAppointments.getCustomerId());
		} catch (ActiveAppointmentsExistsException ex) {
			assertThat(ex.getCustomerId()).isEqualByComparingTo(customerWithAppointments.getCustomerId());

			throw ex;
		}

	}

}
