package com.mycompany.crm.service.impl;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.mycompany.crm.entity.Appointment;
import com.mycompany.crm.entity.Customer;
import com.mycompany.crm.service.api.AppointmentDto;
import com.mycompany.crm.service.api.IdentityAppointmentDto;

public interface AppointmentMapper {
	
	Function<Appointment, AppointmentDto> AS_APPOINTMENT_DTO = appointment -> {
        AppointmentDto appointmentDto = new AppointmentDto();
        
        appointmentDto.setAppointmentStatus(appointment.getAppointmentStatus());
        appointmentDto.setCustomerId(appointment.getCustomer().getCustomerId());
        appointmentDto.setEndTime(appointment.getEndTime());
        appointmentDto.setStartTime(appointment.getStartTime());
        appointmentDto.setVersionId(appointment.getVersion());
        
        return appointmentDto;
    };
    
    Function<Appointment, IdentityAppointmentDto> AS_IDENTITY_APPOINTMENT_DTO = appointment -> {
    	IdentityAppointmentDto appointmentDto = new IdentityAppointmentDto();
        
        appointmentDto.setAppointmentStatus(appointment.getAppointmentStatus());
        appointmentDto.setCustomerId(appointment.getCustomer().getCustomerId());
        appointmentDto.setEndTime(appointment.getEndTime());
        appointmentDto.setStartTime(appointment.getStartTime());
        appointmentDto.setAppointmentId(appointment.getAppointmentId());
        appointmentDto.setVersionId(appointment.getVersion());
        
        return appointmentDto;
    };
    
    
    BiFunction<AppointmentDto,Customer, Appointment > AS_APPOINTMENT = (appointmentDto, customer) -> {
    	
    	Appointment appointment = new Appointment();
    	
    	appointment.setAppointmentStatus(appointmentDto.getAppointmentStatus());
    	appointment.setCustomer(customer);
    	appointment.setEndTime(appointmentDto.getEndTime());
    	appointment.setStartTime(appointmentDto.getStartTime());
    	appointment.setVersion(appointmentDto.getVersionId());
 
        if(appointmentDto instanceof IdentityAppointmentDto) {
        	appointment.setAppointmentId(((IdentityAppointmentDto) appointmentDto).getAppointmentId());
        }
        
        return appointment;
    };

}
