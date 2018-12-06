package com.mycompany.crm.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mycompany.crm.entity.Appointment;
import com.mycompany.crm.entity.AppointmentStatus;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

	public Optional<Appointment> findByStartTimeGreaterThanEqualAndEndTimeLessThanEqualAndAppointmentStatus(
			Instant startTime, Instant endTime, AppointmentStatus appointmentStatus);

	@Query("SELECT a FROM  Appointment a WHERE a.appointmentStatus=:appointmentStatus  AND a.customer.customerId=:customerId")
	public List<Appointment> findByAppointmentStatusAndCustomerId(
			@Param("appointmentStatus") AppointmentStatus appointmentStatus, @Param("customerId") Long customerId);

}
