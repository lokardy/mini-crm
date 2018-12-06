package com.mycompany.crm.service.api;

/**
 * Provides capabilities to work on customer entity.
 * 
 * @author lsubbareddy
 *
 */
public interface CustomerServiceInterface {

	/**
	 * Creates given customer representation
	 * 
	 * @param customerDto
	 * @return {@link IdentityCustomerDto}
	 */
	IdentityCustomerDto createCustomer(CustomerDto customerDto);

	/**
	 * Updates given customer representation
	 * 
	 * @param identityCustomerDto
	 * @return {@link IdentityCustomerDto}
	 */

	IdentityCustomerDto updateCustomer(IdentityCustomerDto identityCustomerDto);

	/**
	 * Finds given customer representationz
	 * 
	 * @param customerId
	 * @return {@link IdentityCustomerDto}
	 */
	IdentityCustomerDto findCustomer(Long customerId);

	/**
	 * Deletes given customer representation
	 * 
	 * @param customerId
	 * @return {@link IdentityCustomerDto}
	 */

	IdentityCustomerDto deleteCustomer(Long customerId);

	/**
	 * Creates appointment for the given customer and time.
	 * 
	 * @param appointmentDto
	 * @return {@link IdentityAppointmentDto}
	 */
	IdentityAppointmentDto createAppointment(AppointmentDto appointmentDto);

	/**
	 * Update appointment status.
	 * 
	 * @param updateAppointmentStatusDto
	 * @return {@link IdentityAppointmentDto}
	 */
	IdentityAppointmentDto updateAppointmentStatus(UpdateAppointmentStatusDto updateAppointmentStatusDto);
}
