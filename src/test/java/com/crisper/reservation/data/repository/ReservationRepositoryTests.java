package com.crisper.reservation.data.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.crisper.reservation.constant.Channel;
import com.crisper.reservation.constant.Status;
import com.crisper.reservation.data.entity.Customer;
import com.crisper.reservation.data.entity.Reservation;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReservationRepositoryTests {
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private CustomerRepository customerRepository;	
		
    @Test
    @Order(1)
    @Rollback(value=false)
	public void saveReservationTest() {

    	Reservation reservation = getReservation();
    	Customer customer = reservation.getCustomer();
    	    	
    	// action
    	customerRepository.save(customer);
    	reservation = reservationRepository.save(reservation);
    	System.out.println(reservation);
    	
    	// verify
    	Assertions.assertThat(reservation).isNotNull();
        Assertions.assertThat(reservation.getId()).isGreaterThan(0);
		
	}
    
    @Test
    @Order(2)
    @Rollback(value=false)
    public void getReservationById(){

        //Action
        Reservation reservation = reservationRepository.findById(1L).get();
        
        //Verify
        Assertions.assertThat(reservation.getId()).isEqualTo(1L);
    }    
    
    @Disabled
    @Test
    @Order(3)
    @Rollback(value = false)
    public void updateEmployeeTest(){

        //Action
        Reservation reservation = reservationRepository.findById(1L).get();
        reservation.setNumberOfGuests(4);
        Reservation reservationUpdated =  reservationRepository.save(reservation);

        //Verify
        Assertions.assertThat(reservationUpdated.getNumberOfGuests()).isEqualTo(4);
    }  
    
    
    private Reservation getReservation() {
    	
    	Reservation reservation = Reservation.builder()
			.reservationDate(LocalDateTime.now())
			.numberOfGuests(2)
			.customer(getCustomer())
			.status(Status.CONFIRMED)
			.build();
    	
    	return reservation;
    } 
    
    private List<Reservation> getReservationList() {
    	
    	List<Reservation> reservations = new ArrayList<Reservation>();
    	
    	Reservation reservation1 = Reservation.builder()
			.reservationDate(LocalDateTime.now())
			.numberOfGuests(2)
			.customer(getCustomer())
			.status(Status.CONFIRMED)
			.build();
    	 
    	Reservation reservation2 = Reservation.builder()
			.reservationDate(LocalDateTime.now())
			.numberOfGuests(3)
			.customer(getCustomer())
			.status(Status.CONFIRMED)
			.build();    	 
    	 
    	Reservation reservation3 = Reservation.builder()
			.reservationDate(LocalDateTime.now())
			.numberOfGuests(4)
			.customer(getCustomer())
			.status(Status.CONFIRMED)
			.build();  
    	
    	reservations.add(reservation1);
    	reservations.add(reservation2);
    	reservations.add(reservation3);
    	
    	return reservations;
    }
    
    private Customer getCustomer() {
    	
    	return Customer.builder()
    			.firstName("Crisper")
    			.lastName("Rosete")
    			.email("crisper.rosete@gmail.com")
    			.phoneNumber("123-456-7890")
    			.channel(Channel.EMAIL)
    			.build();
    }    
     
}
