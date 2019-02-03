# money-transfers-api

A Java RESTful API for money transfers between accounts

### Technologies
- Java 11
- Spark Framework for Micro Services with embedded Jetty Container
- H2 in memory database
- Hibernate ORM
- Log4j
- Apache HTTP Client

### How to run tests 
```sh
mvn test
```
Will run Unit tests, integration tests and end-to-end tests. End-to-end tests will run on the port 4567 by default.

### Create an JAR file

```sh
mvn package
```

### How to start application
```sh
java -jar target/money-transfers-api-jar-with-dependencies.jar
```
Application starts a jetty server on localhost port 8080

### Available Services
| HTTP METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| GET | /api/v1/accounts/{Id} | get account by accountId | 
| POST | /api/v1/accounts | create a new account |
| PUT | /api/v1/accounts/{Id} | update the balance on the existing account |
| DELETE | /api/v1/accounts/{Id} | remove account by accountId | 
| POST | /api/v1/transfers | transfer money between accounts |
| GET | /api/v1/transfers/{Id} | get the result of the transfer |

Note: when using POST method, the location of created resource can be found in the "Location" header of the response

### Http Status
- 200 OK: The request has succeeded
- 201 Created: a new resource has been created
- 400 Bad Request: The request could not be understood by the server 
- 404 Not Found: The requested resource cannot be found
- 500 Internal Server Error: The server encountered an unexpected condition 

### Sample JSON for Accounts and Transfer
##### Create a Source Account :
```sh
{
    "currency" = "GBP", 
    "balance": 1000.00
}
```
##### Create a Destination Account :
```sh
{
    "currency" = "GBP", 
    "balance": 0.00
}
```
##### Transfer funds from Source Account to Destination account
```sh
{
    "currency": "GBP",
    "balance": 100.00,
    "sourceAccount": 1,
    "destinationAccount":2
}
```