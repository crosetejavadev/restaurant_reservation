package com.crisper.reservation.data.entity;

import java.util.ArrayList;
import java.util.List;

import com.crisper.reservation.constant.Channel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="customer")
public class Customer {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false)
    private Long id;	
    
    @Column(name="first_name", nullable=false)
    private String firstName;
    
    @Column(name="last_name", nullable=false)
    private String lastName;
    
    @Column(name="email", nullable=false)
    private String email;    
    
    @Column(name="phone_number", nullable=false)
    private String phoneNumber;   
    
    @Column(name="channel", nullable=false)
    @Enumerated(EnumType.STRING)
    private Channel channel;      
    
    @JsonIgnore
    @OneToMany(mappedBy="customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();
    
    public String getName() {
    	return lastName + ", " + firstName;
    }
    
    public Customer updateCustomerInfo(Customer customer) {
    	this.firstName = customer.firstName;
    	this.lastName = customer.lastName;
    	this.phoneNumber = customer.phoneNumber;
    	this.email = customer.email;
    	this.channel = customer.channel;
    	
    	return this;
    }
}
