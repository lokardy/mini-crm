package com.mycompany.crm.api.test;

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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.mycompany.crm.entity.AppointmentStatus;
import com.mycompany.crm.entity.Customer;
import com.mycompany.crm.repository.AppointmentRepository;
import com.mycompany.crm.repository.CustomerRepository;
import com.mycompany.crm.service.api.AppointmentDto;
import com.mycompany.crm.service.api.CustomerServiceInterface;
import com.mycompany.crm.service.api.IdentityAppointmentDto;
import com.mycompany.crm.service.api.IdentityCustomerDto;
import com.mycompany.crm.service.api.UpdateAppointmentStatusDto;
import com.mycompany.crm.service.impl.CustomerMapper;
import com.mycompany.minicrm.MiniCrmApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = MiniCrmApplication.class)
public class CustomerApiIntegrationTest {

	@Autowired
	private TestRestTemplate template;

	@Autowired
	private CustomerServiceInterface customerService;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AppointmentRepository appointmentRepository;

	private IdentityCustomerDto createdCustomer;

	private Instant startTime = Instant.now();
	private Instant endTime = Instant.now().plus(10, ChronoUnit.MINUTES);

	private IdentityAppointmentDto createdAppointmentDto;

	private IdentityCustomerDto customerWithAppointments;

	@Before
	public void before() {

		appointmentRepository.deleteAll();

		customerRepository.deleteAll();

		Customer customer = new Customer();

		customer.setCustomerName("Lokesh");
		customer.setAddress("testmnbvvvbvbvbvnb");
		customer.setAge(20);
		customer.setVersion(1L);
		customer.setPhoneNumber(9008407323L);

		createdCustomer = customerService.createCustomer(CustomerMapper.AS_CUSTOMER_DTO.apply(customer));

		customer = new Customer();

		customer.setCustomerName("Lokesh");
		customer.setAddress("testhgjghjgh");
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

		appointmentRepository.deleteAll();

		customerRepository.deleteAll();

	}

	@Test
	public void postCustomerShouldReturn409InCaseOfDuplicateMobileNumber() {

		ResponseEntity<IdentityCustomerDto> responseEntity = template.postForEntity("/customers", createdCustomer,
				IdentityCustomerDto.class);

		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CONFLICT);

	}

	@Test
	public void postShouldReturn200ForValidCustomer() {

		createdCustomer.setCustomerName("Lokesh");
		createdCustomer.setPhoneNumber(9008409423L);

		ResponseEntity<IdentityCustomerDto> responseEntity = template.postForEntity("/customers", createdCustomer,
				IdentityCustomerDto.class);

		assertThat(createdCustomer).isEqualToIgnoringGivenFields(responseEntity.getBody(), "customerId", "versionId");

	}

	@Test
	public void putCustomerShouldReturn200ForValidData() {

		createdCustomer.setCustomerName("Lokesh");
		createdCustomer.setPhoneNumber(9008409323L);

		template.put("/customers", createdCustomer, IdentityCustomerDto.class);

	}

	@Test
	public void getCustomerShouldReturnCustomerRepresentationForTheGivenCustomerId() {

		IdentityCustomerDto returnedCustomer = template
				.getForEntity("/customers/" + createdCustomer.getCustomerId(), IdentityCustomerDto.class).getBody();

		assertThat(createdCustomer).isEqualToIgnoringGivenFields(returnedCustomer, "customerId");

	}

	@Test
	public void deleteCustomerShouldDeleteCustomerRepresentationForTheGivenCustomerId() {

		template.delete("/customers/" + createdCustomer.getCustomerId(), IdentityCustomerDto.class);

	}
	
	@Test
	public void getCustomerShouldReturn404IfCustomerNotFound() {

		ResponseEntity<IdentityCustomerDto> responseEntity = template.getForEntity("/customers/" + 10000000L, IdentityCustomerDto.class);
		
		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
		
	}
	
	@Test
	public void postAppointmentForValidDataAndCustomerShouldReturn200() {

		
		AppointmentDto appointmentDto = new AppointmentDto();

		appointmentDto.setAppointmentStatus(AppointmentStatus.ACTIVE);
		appointmentDto.setCustomerId(createdCustomer.getCustomerId());
		appointmentDto.setStartTime(startTime.plus(20, ChronoUnit.MINUTES));
		appointmentDto.setEndTime(endTime.plus(40, ChronoUnit.MINUTES));
		appointmentDto.setVersionId(1L);

		IdentityAppointmentDto createdAppointmentDto = template.postForEntity("/customers/" + createdCustomer.getCustomerId() + "/appointments", appointmentDto, IdentityAppointmentDto.class).getBody();

		assertThat(appointmentDto).isEqualToIgnoringGivenFields(createdAppointmentDto);

	}
	//Todo: Rest template needs special settings to make patch calls
	/*@Test
	public void patchAppointmentShouldReutrn200InCaseOfSuccess() {

		AppointmentDto appointmentDto = new AppointmentDto();

		appointmentDto.setAppointmentStatus(AppointmentStatus.ACTIVE);
		appointmentDto.setCustomerId(createdCustomer.getCustomerId());
		appointmentDto.setStartTime(startTime.plus(50, ChronoUnit.MINUTES));
		appointmentDto.setEndTime(endTime.plus(50, ChronoUnit.MINUTES));

		IdentityAppointmentDto identityAppointmentDto = customerService.createAppointment(appointmentDto);

		UpdateAppointmentStatusDto updateAppointmentStatusDto = new UpdateAppointmentStatusDto(identityAppointmentDto.getAppointmentId(),
				AppointmentStatus.INACTIVE);

		IdentityAppointmentDto updatedAppointmentDto = template.patchForObject("/customers/" + createdCustomer.getCustomerId() + "/appointments/" + identityAppointmentDto.getAppointmentId(), updateAppointmentStatusDto, IdentityAppointmentDto.class);
		
	 Assert.assertEquals(AppointmentStatus.INACTIVE, updatedAppointmentDto.getAppointmentStatus());
	}*/

}
