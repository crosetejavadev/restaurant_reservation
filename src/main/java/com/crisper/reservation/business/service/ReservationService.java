package com.crisper.reservation.business.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crisper.reservation.constant.Channel;
import com.crisper.reservation.constant.Status;
import com.crisper.reservation.data.entity.Customer;
import com.crisper.reservation.data.entity.Reservation;
import com.crisper.reservation.data.repository.CustomerRepository;
import com.crisper.reservation.data.repository.ReservationRepository;
import com.crisper.reservation.exception.ReservationNotFoundException;

@Service
public class ReservationService {

	private ReservationRepository reservationRepository;
	private CustomerRepository customerRepository;
	
	@Autowired
	public ReservationService(ReservationRepository reservationRepository, CustomerRepository customerRepository) {
		this.reservationRepository = reservationRepository;
		this.customerRepository = customerRepository;
	}
	
	public Reservation getReservationById(Long id) {
		Optional<Reservation> existingReservation = reservationRepository.findById(id);
		if (existingReservation.isPresent()) {
			return existingReservation.get();
		} else {
			throw new ReservationNotFoundException("No Reservation exists with ID: " + id);
		}
	}
	
	public List<Reservation> getReservationsByCustomerInfo(Customer customer) {
		Optional<List<Reservation>> existingReservations = 
				reservationRepository.getAllByCustomerInfo(customer);
		if (existingReservations.isPresent()) {
			return existingReservations.get();
		} else {
			throw new ReservationNotFoundException("No Reservations found with Customer Info: " + customer);
		}
	}	
	
	public Reservation saveReservation(Reservation reservation) {
		Optional<Customer> customerOptional = customerRepository.findCustomer(reservation.getCustomer());
		
		if(!customerOptional.isPresent() || customerOptional.isEmpty()) {	
			customerRepository.save(reservation.getCustomer());
		} else {	
			reservation.setCustomer(customerOptional.get());
		}
		
		reservation = reservationRepository.save(reservation);
		notifyCustomer(reservation, reservation.getCustomer().getChannel(), "Reservation has been created.");		
				
		return reservation;
	}	
	
	public Reservation updateReservation(Reservation updatedReservation) {	
		
		Optional<Reservation> existingReservation = reservationRepository.findById(updatedReservation.getId());
		
		if (existingReservation.isPresent()) {
			Reservation reservation = existingReservation.get();
			
			Customer updatedCustomer = updatedReservation.getCustomer();
			Customer customer = reservation.getCustomer();
			
			customer.updateCustomerInfo(updatedCustomer);
			customer = customerRepository.save(customer);
			
			reservation.updateReservationInfo(updatedReservation);
			reservation = reservationRepository.save(reservation);
			
			notifyCustomer(reservation, customer.getChannel(), "Reservation with ID: " + reservation.getId() + " has been updated.");
			
			return reservation;
			
		} else {
			
			throw new ReservationNotFoundException("No Reservation exists with ID: " + updatedReservation.getId());
		}
		
	}
	
	public Reservation cancelReservation(Long id) {

		Optional<Reservation> existingReservation = reservationRepository.findById(id);
		if (existingReservation.isPresent() && existingReservation.get().getStatus().equals(Status.CONFIRMED)) {
			Reservation reservation = existingReservation.get();
			reservation.setStatus(Status.CANCELLED);	
			
			reservation = reservationRepository.save(reservation);
			notifyCustomer(reservation, reservation.getCustomer().getChannel(), "Reservation with ID: " + reservation.getId() + " has been cancelled.");
			
			return reservation;
			
		} else {
			
			throw new ReservationNotFoundException("No Reservation exists with ID: " + id);
		}
	}	
  
    public List<Reservation> getAllBetweenDates(LocalDateTime start, LocalDateTime end) { 
        Optional<List<Reservation>> reservations = reservationRepository.getAllBetweenDates(start, end);  
        return reservations.get(); 
    } 	
    
    public void notifyCustomer(Reservation reservation, Channel channel, String message) {
    	String body = "";
    	
    	switch(channel) {
    		case SMS:
    			body = "Sending SMS to " + reservation.getCustomer().getPhoneNumber();
    			break;
    		case EMAIL:
    			body = "Sending Email to " + reservation.getCustomer().getEmail();
    			break;
    		case ALL:
    			body = "Sending SMS and Email to " + reservation.getCustomer().getName();
    			reservation.setNotified(true);
    			reservationRepository.save(reservation);
    			break;
    	}
    	
    	System.out.println(body + " : " + message);
    }    

}
