# Swisscom Backend Repository (Go and Java)

This is the repository for the code containing the backend implementation of the swisscom assessment.

This repository has two independent parts. One of them in Java and another in Golang.

## Java Project.

To run the java project run the following steps.

```
1. Make sure to have the java, maven and mongodb installed in the system.
2. Make sure mongodb is reachable through port 27017 in localhost.
3. Make the start-springboot.sh script executable. chmod +x start-springboot.sh
4. Make the maven script executable. chmod +x mvnw
5. Run the startup script. ./start-springboot.sh
```

The java project will run on default port 8080.

The project implements complete CRUD functionality for the services api, including GET, GET ALL, PUT, POST and DELETE endpoints.

Sample curl used for testing the project.

```
curl --location 'http://localhost:8080/api/services' \
--header 'Content-Type: application/json' \
--data '{
  "id": "service_id_1",
  "resources": [
    {
      "id": "resource_id_1",
      "owners": [
        {
          "id": "owner_id_1_1",
          "name": "Alice Smith",
          "accountNumber": "AC001",
          "level": 1
        },
        {
          "id": "owner_id_1_2",
          "name": "Bob Johnson",
          "accountNumber": "AC002",
          "level": 2
        }
      ]
    },
    {
      "id": "resource_id_2",
      "owners": [
        {
          "id": "owner_id_2_1",
          "name": "Carol White",
          "accountNumber": "AC003",
          "level": 1
        },
        {
          "id": "owner_id_2_2",
          "name": "David Black",
          "accountNumber": "AC004",
          "level": 2
        }
      ]
    }
  ]
}
'
```

The testcases for the Java APIs are writter in ProjectApplicationTests.java file.

## Golang Project.

The golang project is present in the folder go-project. It implements a batch client to run a batches of requests based on configurable batch size and request URIs.

To run the project follow the below steps.

```
cd go-project
go mod tidy
go run main.go (This will run with default values set in main.go)
```

To configure the values simply pass them as flags while running the main.go file.

```
go run main.go -method GET -url http://localhost:8080/api/services/service_id_1 -batch 50 -steps 5
```

This will call the get method 50 times in 5 steps. 

Allowed flags are

1. batch: Total number of parallel requests to be sent every step.
2. step: Total number of batches in which the parallel requests are to be sent.
3. method: HTTP method of the request.
4. url: URL of the request.
5. body: Body of the request in case of a POST/PUT request.
6. timeout: Time to be waited for each request before expiring the call.

The output will be printed on the terminal.