package com.mycompany.crm.entity;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "APPOINTMENTS")
@NoArgsConstructor
@AllArgsConstructor()
public class Appointment {

	@Id
	@GeneratedValue
	private Long appointmentId;

	private Instant startTime;
	
	private Instant endTime;
	
	private AppointmentStatus appointmentStatus;
	
	@Version
	private Long version;
	
	@ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
	private Customer customer;


}
