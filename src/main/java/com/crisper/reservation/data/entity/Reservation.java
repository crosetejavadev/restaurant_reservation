package com.crisper.reservation.data.entity;


import java.time.LocalDateTime;

import com.crisper.reservation.constant.Channel;
import com.crisper.reservation.constant.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="reservation")
public class Reservation {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;	
    
    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;
    
    @Column(name="res_date", nullable=false)
    private LocalDateTime reservationDate;
    
    @Column(name="guest_count")
    private int numberOfGuests = 1;
    
    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.CONFIRMED;     
    
    @JsonIgnore
    @Column(name="notified")
    private boolean notified = false;       
        
    public Reservation updateReservationInfo(Reservation reservation) {
    	this.numberOfGuests = reservation.numberOfGuests;
    	this.reservationDate = reservation.reservationDate;
    	
    	return this;
    }
    
}
