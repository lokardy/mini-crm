package com.mycompany.crm.entity.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.mycompany.crm.entity.Appointment;
import com.mycompany.crm.entity.AppointmentStatus;
import com.mycompany.crm.entity.Customer;
import com.mycompany.crm.repository.CustomerRepository;
import com.mycompany.minicrm.MiniCrmApplication;

@RunWith(SpringRunner.class)
@DataJpaTest()
@ContextConfiguration(classes = MiniCrmApplication.class)
@Transactional()
public class CustomerEntityTest {

	@Autowired
	private CustomerRepository customerRepository;

	private Customer createdCustomer;

	@Before
	public void before() {

		Customer customer = new Customer();

		customer.setCustomerId(1L);
		customer.setCustomerName("Lokesh");
		customer.setAddress("test");
		customer.setAge(20);
		customer.setVersion(1L);
		customer.setPhoneNumber(9008407323L);
		
		
		Appointment appointment = new Appointment(1L, Instant.now() , Instant.now().plus(10, ChronoUnit.MINUTES), AppointmentStatus.ACTIVE,1L,  customer);
		Appointment appointment1 = new Appointment(1L, Instant.now() , Instant.now().plus(10, ChronoUnit.MINUTES), AppointmentStatus.ACTIVE, 1L, customer);

		customer.getAppointments().add(appointment);
		customer.getAppointments().add(appointment1);
		
		createdCustomer = customerRepository.save(customer);

	}
	
	@Test
	public void shouldReturnCustomerForValidPhoneNumber() {


		Customer createdCustomer = customerRepository.findByPhoneNumber(9008407323L).get();

		assertThat(createdCustomer.getCustomerId()).isNotNull().isNotNegative();

	}
	
	@Test
	public void shouldIReturnNoCustomerForInValidPhoneNumber() {


		Optional<Customer> createdCustomer = customerRepository.findByPhoneNumber(900840732333L);

		assertThat(createdCustomer).isEmpty();

	}

	@Test
	public void shouldInsertCustomerForValidData() {

		Customer customer = new Customer();

		customer.setCustomerId(2L);
		customer.setCustomerName("Lokesh");
		customer.setAddress("test");
		customer.setAge(20);
		customer.setPhoneNumber(9008407323L);
		customer.setVersion(1L);
		
		Appointment appointment = new Appointment(1L, Instant.now() , Instant.now().plus(10, ChronoUnit.MINUTES), AppointmentStatus.ACTIVE, 1L, customer);
		Appointment appointment1 = new Appointment(1L, Instant.now() , Instant.now().plus(10, ChronoUnit.MINUTES), AppointmentStatus.ACTIVE, 1L, customer);

		customer.getAppointments().add(appointment);
		customer.getAppointments().add(appointment1);

		Customer createdCustomer = customerRepository.save(customer);

		assertThat(createdCustomer.getCustomerId()).isNotNull().isNotNegative();

	}

	@Test
	public void shouldUpdateCustomerForValidData() {

		createdCustomer.setCustomerName("Lokesh");

		Customer returnedCustomer = customerRepository.save(createdCustomer);

		assertThat(createdCustomer).isEqualToComparingFieldByFieldRecursively(returnedCustomer);

	}
	
	

	@Test
	public void shouldGetCustomerForValidCustomerId() {

		Customer returnedCustomer = customerRepository.findById(createdCustomer.getCustomerId()).get();

		assertThat(createdCustomer).isEqualToComparingFieldByFieldRecursively(returnedCustomer);

	}

	@Test
	public void shouldDeleteCustomerForValidCustomerId() {
		customerRepository.delete(createdCustomer);

		assertThat(customerRepository.findById(createdCustomer.getCustomerId())).isNotPresent();

	}

}
