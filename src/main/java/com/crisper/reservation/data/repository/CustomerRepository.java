package com.crisper.reservation.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crisper.reservation.data.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

	@Query(nativeQuery = true, value = "select id, first_name, last_name, phone_number, email, channel from customer c where UPPER(first_name) = UPPER(:#{#customer.firstName}) AND UPPER(last_name) = UPPER(:#{#customer.lastName}) or UPPER(email) = UPPER(COALESCE(:#{#customer.email}, '')) or phone_number = COALESCE(:#{#customer.phoneNumber}, '')")
	public Optional<Customer> findCustomer(@Param("customer") Customer customer);	
	
}
