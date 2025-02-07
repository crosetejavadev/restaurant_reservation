package com.crisper.reservation.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.crisper.reservation.business.service.ReservationService;
import com.crisper.reservation.constant.Channel;
import com.crisper.reservation.constant.Status;
import com.crisper.reservation.data.entity.Reservation;

@Component
public class SendNotification {
	
	@Autowired
	private ReservationService reservationService;

	@Scheduled(fixedRate = 15000)
	public void sendNotification() {
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = LocalDateTime.now().plusHours(4);
		List<Reservation> reservations = reservationService.getAllBetweenDates(start, end);
		for(Reservation reservation : reservations) {
			reservationService.notifyCustomer(reservation, Channel.ALL, "The 4-HR Notification Message.");
		}
	}	
	
}
