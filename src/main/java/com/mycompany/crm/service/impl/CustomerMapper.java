package com.mycompany.crm.service.impl;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.mycompany.crm.entity.Customer;
import com.mycompany.crm.service.api.CustomerDto;
import com.mycompany.crm.service.api.IdentityCustomerDto;

//For big projects mappers like Orika can be used
public interface CustomerMapper {
	Function<Customer, CustomerDto> AS_CUSTOMER_DTO = customer -> {
        CustomerDto customerDto = new CustomerDto();
        
        customerDto.setAddress(customer.getAddress());
        customerDto.setAge(customer.getAge());
        customerDto.setCustomerName(customer.getCustomerName());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        customerDto.setVersionId(customer.getVersion());
        
        return customerDto;
    };
    
    Function<Customer, IdentityCustomerDto> AS_CUSTOMER_IDENTITY_DTO = customer -> {
        IdentityCustomerDto customerDto = new IdentityCustomerDto();
        
        customerDto.setAddress(customer.getAddress());
        customerDto.setAge(customer.getAge());
        customerDto.setCustomerName(customer.getCustomerName());
        customerDto.setCustomerId(customer.getCustomerId());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        customerDto.setVersionId(customer.getVersion());
        
        return customerDto;
    };
    
    BiFunction<CustomerDto, Long, IdentityCustomerDto> TO_CUSTOMER_IDENTITY_DTO = (customerDto, customerId) -> {
        IdentityCustomerDto identityCustomerDto = new IdentityCustomerDto();
        
        identityCustomerDto.setAddress(customerDto.getAddress());
        identityCustomerDto.setAge(customerDto.getAge());
        identityCustomerDto.setCustomerName(customerDto.getCustomerName());
        identityCustomerDto.setCustomerId(customerId);
        identityCustomerDto.setPhoneNumber(customerDto.getPhoneNumber());
        identityCustomerDto.setVersionId(customerDto.getVersionId());
        
        return identityCustomerDto;
    };
    
    Function<CustomerDto,Customer> AS_CUSTOMER = customerDto -> {
        Customer customer = new Customer();
        
        customer.setAddress(customerDto.getAddress());
        customer.setAge(customerDto.getAge());
        customer.setCustomerName(customerDto.getCustomerName());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setVersion(customerDto.getVersionId());
        
        if(customerDto instanceof IdentityCustomerDto) {
        	customer.setCustomerId(((IdentityCustomerDto) customerDto).getCustomerId());
        }
        return customer;
    };
    
  
}
