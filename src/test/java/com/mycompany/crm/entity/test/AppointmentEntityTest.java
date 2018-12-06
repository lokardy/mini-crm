package com.mycompany.crm.entity.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
import com.mycompany.crm.repository.AppointmentRepository;
import com.mycompany.crm.repository.CustomerRepository;
import com.mycompany.minicrm.MiniCrmApplication;

@RunWith(SpringRunner.class)
@DataJpaTest()
@ContextConfiguration(classes = MiniCrmApplication.class)
@Transactional()
public class AppointmentEntityTest {
	//TODO : Leaving crud test cases to save time.
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	
	private Customer createdCustomer;
	
	private Appointment createdAppointment;
	
	private Instant startTime = Instant.now();
	private Instant endTime = Instant.now().plus(10, ChronoUnit.MINUTES);
	
	

	@Before
public void beforeEachTestCase() {
		Customer customer = new Customer();

		customer.setCustomerId(1L);
		customer.setCustomerName("Lokesh");
		customer.setAddress("test");
		customer.setAge(20);
		customer.setVersion(1L);
		customer.setPhoneNumber(9008407323L);
		
		//TODO: I don't want to create appointments with customer create 
		//but to make it easier i am leaving it as is. We need to change cascade settings to prevent the same
		
		Appointment appointment = new Appointment(1L, startTime , endTime, AppointmentStatus.ACTIVE, 1L, customer);
		customer.getAppointments().add(appointment);

		createdCustomer = customerRepository.save(customer);
		createdAppointment = createdCustomer.getAppointments().iterator().next();
}
	
	@Test
	public void  shouldReturnAllActiveAppointmentsForTheGivenCustomer() {
		List<Appointment> activeAppointments = appointmentRepository.findByAppointmentStatusAndCustomerId(AppointmentStatus.ACTIVE, createdCustomer.getCustomerId());
		
		assertThat(activeAppointments).isNotEmpty().hasSize(1).containsExactly(createdAppointment);
	}
	
	@Test
	public void  shouldReturnAllAppointmentsForTheGivenTimePeriodForTheGivenCustomer() {
		Appointment appointment = appointmentRepository.
				findByStartTimeGreaterThanEqualAndEndTimeLessThanEqualAndAppointmentStatus(startTime, endTime, AppointmentStatus.ACTIVE).get();
		
		assertThat(appointment).isEqualToComparingFieldByFieldRecursively(createdAppointment);
	}
	
	@Test
	public void  shouldReturnNothingForInvalidTimeRangeAndCustomer() {
		Optional<Appointment> appointment = appointmentRepository.
				findByStartTimeGreaterThanEqualAndEndTimeLessThanEqualAndAppointmentStatus(Instant.now().plus(20, ChronoUnit.MINUTES), Instant.now().plus(30, ChronoUnit.MINUTES), AppointmentStatus.ACTIVE);
		
		assertThat(appointment).isEmpty();
	}



}
