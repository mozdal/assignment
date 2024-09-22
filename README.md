# Insider Assignment

This is the source code for the Insider assignment. The assignment is to create a simple application that displays fetches unsent messages from the database and makes an external service call to send the messages to their respective mobile numbers.

## Application Components

Java 17 and Spring Boot Framework is used to create the application. MariaDB was used for database purposes and Redis is used for caching.

## Installation

The application uses docker-compose to run the MariaDB and Redis containers. To run the application, you need to have Docker and Docker Compose installed on your machine. Java application is also dockerized with the multi-stage build. To run the application simply call

```docker compose up```

from the root directory of the project. This will start the MariaDB, Redis, and Java application containers.

## External Service Mock

Beeceptor was used to create a mock external service. The service is a simple POST endpoint that accepts a JSON object and returns a 202 Accepted response. A sample response of from this mock can be seen below.

```
{
"message": "Accepted",
"messageId": "6d1dc335-68b7-4567-a780-93b5b39784b2"
}
```

The URL of the service is: [https://assignment.beeceptor.com/](https://assignment.beeceptor.com/)

> **Note:** Since Beeceptor is a free mock service, it has a rate limit that is fairly low. If you start getting errors from the service (429 Too Many Requests), you can switch to the backup mock service, [https://deneme.beeceptor.com/](https://deneme.beeceptor.com/). You can change the environment variable `EXTERNAL_MESSAGE_SERVICE_URL` in the `docker-compose.yml` file to switch between the services.

## API Endpoints

The application has three API endpoints:

1. **GET** /message -> returns all sent messages from the database
2. **GET** /state/start -> starts the message sending process
3. **GET** /state/stop -> stops the message sending process

You can also reach the Swagger UI from the following URL:

[Swagger UI](http://localhost:8081/swagger-ui/index.html)

## Application Logic

There are two schedulers running on the application. The first scheduler, MessageJob works every 2 minutes and triggers a process method that fetches oldest 2 records with 'UNSENT' status from the db and makes an async call to external service. The second scheduler, MessageCreateJob is a helper job that works every minute. It checks if there are any db records with 'UNSENT' status and if there is not, create 20 new random db records to help main job continue executing.
