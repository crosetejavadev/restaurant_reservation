## spring-boot rest api

# Restaurant Reservation System
The following are the Assumptions and Design Decisions made based on Project Requirements

### Assumptions
- All fields are not nullable in the database.
  The user input will not be validated but an exception will be thrown when a reservation record is saved with a field having an empty or null value.
  
- One or more Customer Information can be used to retrieve existing customer reservations.
  The reservations will be retrieved with the Customer's first and last name, phone number, or email.

- Notification schedule.
  Notifications will be sent upon **create**, **update**, and **cancellation** of a reservation to the selected communication channel apart from the notifications to be sent out 4 hours before the reservation date.

- Reservation Cancellation.
  Cancellation can only be done one reservation at a time.

- Duplicate reservations.
  Every reservation saved has a unique ID. No validation if a duplicate reservation (same time or number of guests) has been saved. 

- Overbooking.
  The system will not have a limit on the reservations created.

- Reservation requirements.
  No validation is required for creating, updating and canceling reservation records.



### Design Decisions

- Originally, the reservation was treated as a single entity. 
However, based on the requirements, customers can now have multiple reservations, so the customer information can be stored in a separate table. Separating the reservation and customer information will be more efficient as the records increase in size.No repeataing data will be saved in a single table.

- Cancelled reservations are only soft deleted. They are not deleted physically in the database. So the cancel request is only to update the status field in reservation to CANCELLED. They stay in the database for future inventory and data audit.

- For demonstration purposes, the Service Class is implemented as a concrete class rather than an interface. In the implemented design, the Controller and Service layers are loosely coupled using Spring's IOC and DI. Testing all the Spring layers was straightforward, thanks to the mocking libraries that allow the creation of test stubs.

- For simplicity, 2 types of Exceptions are thrown.
If a particular record is not found.
As a generic exception handler, Internal Server Error will be returned for all other exceptions.

- Retrieving reservations will have 2 flavors - by ID(single record) or by Customer Information(multiple records).


### Future Features

- Multiple cancellation.

- Completed Status for fulfilled reservations.

- Data Input Validation
