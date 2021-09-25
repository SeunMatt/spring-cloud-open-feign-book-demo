# Spring Cloud OpenFeign Book Demo

[![Build Status](https://travis-ci.org/SeunMatt/mysql-backup4j.svg?branch=master)](https://travis-ci.org/SeunMatt/mysql-backup4j)

This is the complete source code for the Spring Cloud OpenFeign Book

# Running Tests Locally

### Step 1: Start the Server Application

Before you can run all the tests locally, you should start the `demodiscovery` and `demoserver` applications. 
From the root directory of this project execute the following commands:

- First build a fresh jar: `./mvnw clean package -DskipTests=true`
- Then start the `demodiscovery` application: `java -jar ./demodiscovery/target/demodiscovery.jar`
- Then start the `demoserver` application: `java -jar ./demoserver/target/demoserver.jar`

Alternatively, you can start both applications from your favourite IDE or using Docker compose by running `docker-compose -f ./docker-compose.yml up --build`

### Step 2: Run the tests

Having starts the server, you can execute the following command from the project's root directory:
`./mvnw clean test -f democonsumer/pom.xml`