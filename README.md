# Recipe Management API

This project is a standalone Java application designed to manage users' favorite recipes. It provides RESTful APIs for adding, updating, removing, and fetching recipes. The application also includes advanced filtering capabilities to search for recipes based on various criteria, such as whether the recipe is vegetarian, the number of servings, specific ingredients (include or exclude), and text search within the instructions.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Setup and Running](#setup-and-running)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Author](#author)

## Features

- **CRUD Operations**: Add, update, delete, and fetch recipes.
- **Advanced Filtering**: Search for recipes based on:
  - Whether the dish is vegetarian.
  - The number of servings.
  - Including or excluding specific ingredients.
  - Text search within the instructions.
- **Database Persistence**: All data is persisted in a PostgreSQL database.
- **API Documentation**: Automatically generated API documentation using OpenAPI/Swagger.
- **Unit and Integration Tests**: Comprehensive tests to ensure code quality and reliability.

## Technology Stack

- **Java 21**
- **Spring Boot 3.3.2**
- **Spring Data JPA**: For database operations.
- **PostgreSQL**: As the database.
- **Flyway**: For database migrations.
- **Springdoc OpenAPI**: For API documentation.
- **JUnit 5**: For unit and integration tests.

## Setup and Running

### Prerequisites

- **Java 21**: Ensure you have Java 21 installed.
- **Maven**: Make sure Maven is installed for managing dependencies and building the project.
- **PostgreSQL**: Set up a PostgreSQL database.

### Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Alireza-mousavinasab/ABN-AMRO-Assessment.git
   cd recipe-management
   
2. **Configure the Database**
Update the application.properties file located in src/main/resources with your PostgreSQL database details:

       spring.datasource.url=jdbc:postgresql://localhost:5432/recipeDB
       spring.datasource.username=username
       spring.datasource.password=password
       spring.jpa.hibernate.ddl-auto=update

3. **Run the Application**
You can run the application using Maven:

       mvn spring-boot:run

The application will start on http://localhost:8080.

## Usage

Once the application is running, you can access the API endpoints through any HTTP client like Postman or directly via the browser.
Example Endpoints

    Create a new recipe: POST /api/recipes
    Update a recipe: PUT /api/recipes/{id}
    Delete a recipe: DELETE /api/recipes/{id}
    Get all recipes: GET /api/recipes
    Search recipes: GET /api/recipes/search?isVegetarian=true&servings=4&includeIngredients=potatoes

## API Documentation

The API documentation is available at http://localhost:8080/swagger-ui/index.html once the application is running. It provides detailed information on all the available endpoints and their usage.

## Testing

The project includes comprehensive unit and integration tests to ensure that the code is reliable and functions as expected.

 - Unit Tests: Test individual components of the application.
 - Integration Tests: Test the application as a whole, including interactions with the database.

You can run the tests using Maven:

    mvn test
