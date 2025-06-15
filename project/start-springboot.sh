#!/bin/bash

# Exit on error
set -e

# Build the project (skip tests if you want faster startup)
./mvnw clean package -DskipTests

# Run the Spring Boot application
java -jar target/*.jar