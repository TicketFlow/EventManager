# EventManager
The event management service is responsible for managing events registered on the EventWave platform. It allows users to create, update and delete events, as well as view details of events and their tickets.
Endpoints

### Endpoints
* **/events** (GET) - endpoint to list all registered events.
* **/events/{id}** (GET) - endpoint to view details of a specific event.
* **/events** (POST) - endpoint to create a new event.
* **/events/{id}** (PUT) - endpoint to update an existing event.
* **/events/{id}** (DELETE) - endpoint to delete an existing event.

### Dependencies
*	Spring Boot
*	Spring Data JPA
*	Flyway
*	MongoDB
*	PostgreSQL
