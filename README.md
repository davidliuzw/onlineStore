# README.md

## Project Overview

This project is a highly scalable online store management system, designed and developed using a range of modern technologies and best practices in software engineering. The system supports core functionalities for managing users, categories, shops, and products, and includes features such as user authentication, REST APIs, and a recommendation system based on machine learning algorithms.

## Technologies Used

### Backend Technologies

| Technology         | Details                  |
|--------------------|--------------------------|
| Programming Language | Java                    |
| Frameworks          | Spring MVC, Spring Security |
| Build Tool          | Maven                    |
| Version Control     | Git                      |
| Server              | Tomcat                   |
| Containerization    | Docker                   |
| API Testing         | Postman                  |
| Database            | MySQL                    |
| ORM                 | MyBatis                  |
| Architecture        | Micro-Service            |


### Additional Tools
- **Connection Pooling**: C3P0
- **Circuit Breaker**: Netflix Hystrix
- **Monitoring**: Spring Actuator
- **Authentication**: Cookie-Session, JWT, Redis

## Features

### Database Design
- Designed a MySQL database schema with tables for `user`, `categories`, `shops`, and `products`.
- Utilized C3P0 connection pool to efficiently handle various database queries.

### REST APIs
- Implemented RESTful APIs to create, update, and delete users, shops, and products.
- Developed a Data Access Object (DAO) layer to map SQL commands to the corresponding CRUD actions.

### User Authentication
- Developed backend services for user authentication using a combination of Cookie-Session mechanisms, JWT, and Redis.

### Circuit Breaker and Monitoring
- Integrated Netflix Hystrix as a circuit breaker to handle failures gracefully.
- Used Spring Actuator to monitor application health and metrics.

### Recommendation System
- Implemented a recommendation system using machine learning algorithms such as Principal Component Analysis (PCA) and k-nearest neighbors (KNN).
- Grouped similar users and stores to provide personalized recommendations.

## Installation and Setup

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. **Build the Project**
   ```bash
   mvn clean install
   ```

## Usage

1. **API Endpoints**: Use Postman or any other API testing tool to interact with the REST APIs.
2. **Authentication**: Implement the authentication flow using cookies or JWT as needed.
3. **Monitoring**: Access Spring Actuator endpoints to monitor the application's health and metrics.

## Contributing

We welcome contributions to enhance the online store management system. Please fork the repository and submit a pull request with your changes.
