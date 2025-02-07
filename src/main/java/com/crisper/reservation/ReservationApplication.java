package com.crisper.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;


@OpenAPIDefinition (info =
@Info(
          title = "REST API for the Restaurant Reservation System",
          version = "0.1",
          description = "This is a demo Swagger Spec for the sample REST API used by the Restaurant Reservation System. <br/>The system is used to manage table reservations to help restaurant owners, managers, and staff provide a smooth, efficient, and customer-friendly dining experience. "
  )
)
@EnableScheduling
@SpringBootApplication
public class ReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationApplication.class, args);
	}
}
