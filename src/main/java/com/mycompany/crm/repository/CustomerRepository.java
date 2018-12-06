package com.mycompany.crm.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.crm.entity.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	public Optional<Customer> findByPhoneNumber(Long phoneNumber);
}
