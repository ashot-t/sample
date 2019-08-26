# Simple shopping card application with Spring Security and JWT 


In this simple application, the swagger UI and H2 database console configured out of the box and sample data will be loaded every time when application starts by Liquibase.Two users will be loaded by Liquibase on every start: 
	1. login: admin@localhost, password: admin, Role: ADMIN
	2. login: customer@localhost, password: customer, Role: CUSTOMER
Swagger UI would be available on URL: http://<ip_address>:8080/swagger-ui.html .
In memory H2 database web console would be available on URL: http://<ip_address>:8080/h2-console.  Please find credentials below:
	1. Driver Class: org.h2.Driver
	2. JDBC URL: jdbc:h2:mem:shoppingcart
	3. User Name: shoppingcart
	4. Password: 

## Authentication and authorization
1. Get the JWT based token from the authentication endpoint, eg `/auth/signin`. 
2. Extract token from the authentication result.
3. Set the HTTP header `Authorization` value as `Bearer jwt_token`.
4. Then send a request to access the protected resources. 
5. If the JWT token is valid it will return the requested resource to client.

## New user registration
New user registration could be done by sending POST request to registration endpoint, eg `/auth/signup`


## Running the application
mvn spring-boot:run or in an IDE by running spring boot application.