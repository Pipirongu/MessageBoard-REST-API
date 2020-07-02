The solution uses 
* Spring Framework
* Spring Data JPA to communicate with the database using Java code(Mapping java class/fields to database)
* H2 database as In-memory solution
* Swagger for UI and quick testing of endpoints

I used Postman to further test the endpoints.
I used a Windows machine this time to code and the fastest way for me was to debug/run directly in IntelliJ. But I have also verified that the solution can be run with maven using mvn spring-boot:run command.

The H2 database can be quickly accessed using http://localhost:8080/h2
JDBC URL: jdbc:h2:mem:test
User Name: sa

The endpoints can be viewed with swagger http://localhost:8080/swagger-ui.html#

The message is represented as follow:

{
  "id": 0,
  "message": "string",
  "owner": "string"
}

* The Id needs to be unique and is used to identify a message.
* The owner is the client, this is a way for me to identify that a message belongs to a certain client. 
* The message is just the message

Additional info:
The endpoint to create a message will not succeed if there is a message with the same Id. Responds with 409 Conflict
Updating/Removing a message will not succeed if there's no message with given Id. Or if Id exists but the owner doesn't match. Responds with 404 Not Found

