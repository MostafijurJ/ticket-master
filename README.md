## Documentations
This project is a Spring Boot application that serves as a gateway service. It uses Spring Cloud Gateway to route requests to different microservices and includes rate limiting configuration.


## Key Features

### Search & Viewing
- Fast event retrieval with distributed caching.
- Low-latency queries using Elasticsearch.
- High availability with sharded event catalog database.

### Booking Consistency
- Transactional integrity for bookings.
- Distributed locking to prevent double bookings.
- Eventual consistency for non-critical data.

### Scalability
- Horizontal scaling for services and databases.
- Efficient ticket inventory management with partitioning.
- Queue-based booking for high request volumes.

### Low Latency Search
- Optimized search infrastructure with indexed fields.
- Edge caching with CDNs.
- Pre-computed aggregations.

### High Read Throughput
- Database load distribution with read replicas.
- API layer query result caching.
- High read velocity with NoSQL integration.

### Resilience & Fault Tolerance
- Multi-region deployment for disaster recovery.
- Graceful degradation during peak loads.
- Automated scaling and health checks.

### Popular Events Optimization
- Hot shard strategy.
- API rate limiting for fair resource distribution.

### User-Friendly
- Guest checkout support.
- Real-time ticket availability updates.
- Email/SMS notifications for confirmations and reminders.



## Prerequisites
### Running Redis with Docker
To run Redis using Docker, you can use the following commands:

```sh
docker pull redis
docker run --name redis -p 6379:6379 -d redis
