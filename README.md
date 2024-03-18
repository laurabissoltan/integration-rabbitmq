# Microservices Architecture with RabbitMQ Integration

This project demonstrates a microservices 
architecture using Spring Boot, where Core, Payment, Order, and Mailing services 
communicate asynchronously through RabbitMQ. This architecture is designed to process orders, handle payments, 
and manage email notifications efficiently.

### Architecture Overview

Core Service: Manages the application's database interactions, including storing and retrieving order and payment information.
Order Service: Handles order creation. Sends messages to core service to save the order.
Payment Service: Processes payments and communicates with the Core service to check and update payment status.
Mailing Service: Sends email notifications related to order and payment events.

RabbitMQ facilitates messaging between these services, ensuring loose coupling and scalable communication.

### Running the Application
1. Clone the repository and navigate to the project directory.
2. Build the application using Maven: mvn clean install
3. Run the application: java -jar target/your-application.jar
Alternatively, you can use the Spring Boot Maven plugin: mvn spring-boot:run

### Mailing Service Email Setup
Replace yourEmail@gmail.com and <YourEmailPassword> in the application
properties (mailing service) with your actual email and password.
For enhanced security, especially if Two-Factor Authentication (2FA) is enabled on your Gmail account, 
it's recommended to use an App Password for SMTP authentication:
1. Go to your Google Account's App Passwords page.
2. Select Mail as the app and Other as the device, then generate.
3. Use the generated 16-character code as your password in the mail configuration.