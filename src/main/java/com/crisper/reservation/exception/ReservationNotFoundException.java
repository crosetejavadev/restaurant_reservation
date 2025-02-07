package com.crisper.reservation.exception;

import org.springframework.http.HttpStatus;

public class ReservationNotFoundException extends RuntimeException {

		public ReservationNotFoundException(String message) {
			super(message);
		}

}
