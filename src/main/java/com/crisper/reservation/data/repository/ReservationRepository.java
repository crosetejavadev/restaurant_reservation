package com.crisper.reservation.data.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crisper.reservation.data.entity.Customer;
import com.crisper.reservation.data.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

	@Query(nativeQuery = true, value = "select id, customer_id, res_date, guest_count, notified, status from reservation t where res_date BETWEEN :startDate AND :endDate and notified = false")
	public Optional<List<Reservation>> getAllBetweenDates(@Param("startDate") LocalDateTime startDate,@Param("endDate") LocalDateTime endDate);	
	
	@Query(nativeQuery = true, value = "select  res.id, res.customer_id, res.notified, res.guest_count, res.res_date, res.status from reservation res, customer cus where res.customer_id = cus.id and UPPER(status) = 'CONFIRMED' and (UPPER(cus.first_name) = UPPER(:#{#customer.firstName}) AND UPPER(cus.last_name) = UPPER(:#{#customer.lastName}) or UPPER(cus.email) = UPPER(COALESCE(:#{#customer.email}, '')) or cus.phone_number = COALESCE(:#{#customer.phoneNumber}, ''))")
	public Optional<List<Reservation>> getAllByCustomerInfo(@Param("customer") Customer customer);	
	
}
