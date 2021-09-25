# Spring Cloud OpenFeign Book Demo

This is the complete source code for the Spring Cloud OpenFeign Book by Seun Matt.

# Running Tests Locally

### Step 1: Start the Server Application

Before you can run all the tests locally, you should start the `demodiscovery` and `demoserver` applications. 
From the root directory of this project execute the following commands:

- First build a fresh jar: `./mvnw clean package -DskipTests=true`
- Then start the `demodiscovery` application: `java -jar ./demodiscovery/target/demodiscovery.jar`
- Then start the `demoserver` application: `java -jar ./demoserver/target/demoserver.jar`

Alternatively, you can use Docker compose to start both applications by running `docker-compose -f ./docker-compose.yml up --build` or use your favourite IDE 

### Step 2: Run the tests

Having starts the server, you can execute the following command from the project's root directory:
`./mvnw clean test -f democonsumer/pom.xml`