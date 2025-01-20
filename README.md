## Documentations
This project is a Spring Boot application that serves as a gateway service. It uses Spring Cloud Gateway to route requests to different microservices and includes rate limiting configuration.

### Key Components

- **GatewayConfig.java**: Configures the routes for the gateway service.
  - Routes requests to the Event Service and Search Service.
  - Strips the prefix from the request path before forwarding.

- **RateLimiterConfig.java**: Configures rate limiting for the gateway service.
  - Uses the client IP address to resolve the key for rate limiting.

### Running Redis with Docker

To run Redis using Docker, you can use the following commands:

```sh
docker pull redis
docker run --name redis -p 6379:6379 -d redis
