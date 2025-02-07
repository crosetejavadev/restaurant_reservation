package com.crisper.reservation.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crisper.reservation.business.service.ReservationService;
import com.crisper.reservation.data.entity.Customer;
import com.crisper.reservation.data.entity.Reservation;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value="/")
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;

	@Operation(summary = "Creates a new reservation.")
    @PostMapping("/reservation")
    public ResponseEntity<Object> saveReservation(@RequestBody Reservation reservation) {
        reservation = reservationService.saveReservation(reservation);
        return new ResponseEntity<Object>(reservation, HttpStatus.CREATED);
    }	
	
	@Operation(summary = "Retrieves a reservation record by ID.")
	@RequestMapping(method=RequestMethod.GET, value="/reservation/{id}")
	public ResponseEntity<Reservation> getReservationById(@PathVariable(value="id") Long id) {
		Reservation reservation = reservationService.getReservationById(id);
		return ResponseEntity.ok(reservation);
	}
	
	@Operation(summary = "Retrieves a list of reservations record by customer information.")
	@RequestMapping(method=RequestMethod.GET, value="/reservations")
	public ResponseEntity<List<Reservation>> getReservationsByCustomerInfo(@RequestBody Customer customer) {
		List<Reservation> reservations = reservationService.getReservationsByCustomerInfo(customer);
		return ResponseEntity.ok(reservations);
	}	
    
	@Operation(summary = "Updates a reservation record.")
	@PutMapping("/reservation")
	public ResponseEntity<Reservation> updateReservation(@RequestBody Reservation updatedReservation) {
		Reservation reservation = reservationService.updateReservation(updatedReservation);
		return ResponseEntity.ok(reservation);
	}	
    
	@Operation(summary = "Cancels a reservation by ID.")
    @PutMapping("/reservation/cancel/{id}")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id) {
        Reservation cancelledReservation = reservationService.cancelReservation(id);
        return ResponseEntity.ok(cancelledReservation);
    }   
}
