package com.crisper.reservation.web.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.crisper.reservation.business.service.ReservationService;
import com.crisper.reservation.constant.Channel;
import com.crisper.reservation.constant.Status;
import com.crisper.reservation.data.entity.Customer;
import com.crisper.reservation.data.entity.Reservation;
import com.crisper.reservation.data.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ReservationController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationControllerTests {

    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private ReservationService reservationService;
    
    @MockitoBean
    private CustomerRepository customerRepository;    
    
    @Autowired
    private ObjectMapper objectMapper;  
    
    Reservation reservation;
    
    @BeforeEach
    void setup() {
    	
    	Customer customer = Customer.builder().id(1L)
			.firstName("Crisper")
			.lastName("Rosete")
			.email("crisper.rosete@gmail.com")
			.phoneNumber("123-456-7890")
			.channel(Channel.EMAIL)
			.build();   	
    	
    	reservation = Reservation.builder().id(1L)
			.reservationDate(LocalDateTime.now())
			.numberOfGuests(2)
			.customer(getCustomer())
			.status(Status.CONFIRMED)
			.build();    	
    }
    
    //POST Controller
    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveReservationTest() throws Exception {
        
    	// mock
    	Reservation savedReservation = getReservation();
    	
    	// precondition
        given(reservationService.saveReservation(any(Reservation.class))).willReturn(this.reservation);
        
        // action
        ResultActions response = mockMvc.perform(post("/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedReservation)));        
        
        // verification
        response.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(this.reservation.getId().intValue())))
        		.andExpect(jsonPath("$.customer.id", is(this.reservation.getCustomer().getId().intValue())));
    }
    
    //GET Controller
    @Test
    @Order(2)
    public void getReservationById() throws Exception {
    	
    	// mock
    	Reservation reservation = getReservation();

        // precondition
        given(reservationService.getReservationById(reservation.getId())).willReturn(this.reservation);

        // action
        ResultActions response = mockMvc.perform(get("/reservation/{id}", reservation.getId()));
        
        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(this.reservation.getId().intValue())));
    
    }
    
    //GET Controller
    @Test
    @Order(3)
    public void getReservationsByCustomerInfo() throws Exception {
    	
    	// mock
    	Customer customer = getCustomer();
    	List<Reservation> reservations = getReservationList();
    	Reservation reservation = getReservation();

        // precondition
        given(reservationService.getReservationsByCustomerInfo(any(Customer.class))).willReturn(reservations);

        // action
        ResultActions response = mockMvc.perform(get("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)));        
        
        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(3)));
    
    }    
    
    //PUT Controller
    @Test
    @Order(4)
    public void updateReservation() throws Exception {
    	
    	// mock
    	Reservation reservation = getReservation();
        reservation.setNumberOfGuests(5);
        reservation.setReservationDate(LocalDateTime.of(2025, 3, 15, 04, 30, 10)); 

        this.reservation.setNumberOfGuests(5);
        this.reservation.setReservationDate(LocalDateTime.of(2025, 3, 15, 04, 30, 10)); 
        
        // precondition
        given(reservationService.updateReservation(any(Reservation.class))).willReturn(this.reservation);
        
        // action
        ResultActions response = mockMvc.perform(put("/reservation", reservation.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservation)));    
        
        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.numberOfGuests", is(this.reservation.getNumberOfGuests())))
                .andExpect(jsonPath("$.reservationDate", is(this.reservation.getReservationDate().toString())));
    
    }
    
    //PUT Controller
    @Test
    @Order(5)
    public void cancelReservation() throws Exception {  	
    	
    	// mock
    	Reservation reservation = getReservation();
    	reservation.setStatus(Status.CANCELLED);
    	
    	this.reservation.setStatus(Status.CANCELLED);
    	
        // precondition
        given(reservationService.cancelReservation(reservation.getId())).willReturn(this.reservation);
        
        
        // action
        ResultActions response = mockMvc.perform(put("/reservation/cancel/{id}", reservation.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservation)));         
  
        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.status", is(reservation.getStatus().name())));

    }    
    
    
    private Reservation getReservation() {
    	
    	Reservation reservation = Reservation.builder().id(1L)
			.reservationDate(LocalDateTime.now())
			.numberOfGuests(2)
			.customer(getCustomer())
			.status(Status.CONFIRMED)
			.build();
    	
    	return reservation;
    } 
    
    private List<Reservation> getReservationList() {
    	
    	List<Reservation> reservations = new ArrayList<Reservation>();
    	
    	Reservation reservation1 = Reservation.builder().id(3L)
			.reservationDate(LocalDateTime.now())
			.numberOfGuests(2)
			.customer(getCustomer())
			.status(Status.CONFIRMED)
			.build();
    	 
    	Reservation reservation2 = Reservation.builder().id(4L)
			.reservationDate(LocalDateTime.now())
			.numberOfGuests(3)
			.customer(getCustomer())
			.status(Status.CONFIRMED)
			.build();    	 
    	 
    	Reservation reservation3 = Reservation.builder().id(5L)
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
    	
    	return Customer.builder().id(2L)
    			.firstName("Crisper")
    			.lastName("Rosete")
    			.email("crisper.rosete@gmail.com")
    			.phoneNumber("123-456-7890")
    			.channel(Channel.EMAIL)
    			.build();
    }
    
}
