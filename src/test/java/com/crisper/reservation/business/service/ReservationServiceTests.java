package com.crisper.reservation.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

import com.crisper.reservation.constant.Channel;
import com.crisper.reservation.constant.Status;
import com.crisper.reservation.data.entity.Customer;
import com.crisper.reservation.data.entity.Reservation;
import com.crisper.reservation.data.repository.CustomerRepository;
import com.crisper.reservation.data.repository.ReservationRepository;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationServiceTests {
	
    @Mock
    ReservationRepository reservationRepository;
    
    @Mock
    CustomerRepository customerRepository;    
    
    @InjectMocks
    ReservationService reservationService;
    
    Customer customer;
    Reservation reservation;
    
    @BeforeEach
    public void setupReservation(){

    	customer = Customer.builder()
    			.id(1L)
    			.firstName("Crisper")
    			.lastName("Rosete")
    			.email("crisper.rosete@gmail.com")
    			.phoneNumber("1234567890")
    			.channel(Channel.EMAIL)
    			.build();
    	
    	customerRepository.save(customer);
    	
    	reservation = Reservation.builder()
    			.id(1L)
    			.reservationDate(LocalDateTime.now())
    			.numberOfGuests(3)
    			.status(Status.CONFIRMED)
    			.customer(customer)
    			.build();

    }   
    
    @Test
    @Order(1)
    public void saveReservationTest(){
        // precondition
        given(reservationRepository.save(reservation)).willReturn(reservation);

        //action
        Reservation savedReservation = reservationService.saveReservation(reservation);

        // verify 
        System.out.println(savedReservation);
        assertThat(savedReservation).isNotNull();
    }   
    
    @Test
    @Order(2)
    @Rollback(value = false) 
    public void getReservationByIdTest() {
    	
    	given(reservationRepository.findById(reservation.getId())).willReturn(Optional.of(reservation));    	
    	
    	Reservation retrievedReservation = reservationService.getReservationById(reservation.getId());

    	assertThat(retrievedReservation).isNotNull();
    	assertThat(retrievedReservation.getId()).isEqualTo(1L);
    }     
    
    @Test
    @Order(3)
    @Rollback(value = false) 
    public void getReservationsByCustomerTest() {
    	
    	List<Reservation> reservationList = new ArrayList<Reservation>();
    	reservationList.add(reservation);
    	
    	given(reservationRepository.getAllByCustomerInfo(customer)).willReturn(Optional.of(reservationList));    	
    	
    	Optional<List<Reservation>> reservations = reservationRepository.getAllByCustomerInfo(customer);

    	assertThat(reservations.get()).isNotNull();
    	assertThat(reservations.get().get(0).getId()).isEqualTo(1L);
    }
    
   
    
    @Test
    @Order(4)
    @Rollback(value = false)    
    public void cancelReservationTest() {
    	
        given(reservationRepository.findById(reservation.getId())).willReturn(Optional.of(reservation));
    	
        Customer customer = Customer.builder()
    			.id(1L)
    			.firstName("Crisper")
    			.lastName("Rosete")
    			.email("crisper.rosete@gmail.com")
    			.phoneNumber("1234567890")
    			.channel(Channel.EMAIL)
    			.build();
    	
    	customerRepository.save(customer);
    	
    	Reservation cancelledReservation = Reservation.builder()
    			.id(1L)
    			.reservationDate(LocalDateTime.now())
    			.numberOfGuests(3)
    			.status(Status.CANCELLED)
    			.customer(customer)
    			.build();
        given(reservationRepository.save(any(Reservation.class))).willReturn(cancelledReservation);
        
        // action
        Reservation cancelled = reservationService.cancelReservation(reservation.getId());
        
        //verify
 	    assertThat(cancelled.getStatus()).isEqualTo(cancelledReservation.getStatus());
    }
}
