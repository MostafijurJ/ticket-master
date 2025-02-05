## Technical Documentations

This project is a Spring Boot application that serves as a gateway service. It uses Spring Cloud Gateway to route
requests to different microservices and includes rate limiting configuration.

## Key Features

### Search & Viewing

- Fast event retrieval with distributed caching.
- Low-latency queries using Elasticsearch.
- High availability with sharded event catalog database.
- Email notifications for new events. All the existing users who have subscribed to the event.
- Report Module for the Admin to view the reports of the events and the bookings.

### Booking Consistency

- Transactional integrity for bookings.
- Distributed locking to prevent double bookings.
- Eventual consistency for non-critical data.

### User-Friendly

- Guest checkout support.
- Real-time ticket availability updates.
- Email/SMS notifications for confirmations and reminders.
- Report Module for the Admin to view the reports of the events and the bookings.

## Prerequisites

### Running Redis with Docker

To run Redis using Docker, you can use the following commands:

```sh
docker pull redis
docker run --name redis -p 6379:6379 -d redis
```


## Notes for Payment Service 
### use strategy pattern for payment gateway
- Payment Strategy has 3 methods 
  - collectPaymentDetails()
  - validatePaymentDetails()
  - processPayment()

## Notes for Notification Service
- Notification Service has 2 methods
  - sendSMS()
  - sendEmail()