# GitHubApi

*[General info](#general-info)
*[Technologies](#technologies)
*[Usage](#Usage)

## General info

The objective of this project is to create an API that allows consumers to retrieve information about user repositories from GitHub.
The API should support JSON format and provide appropriate responses according to the requirements.

## Technologies
Project is created witch:
* Java version 17
* Spring Boot 3.1.0
* Spring Dependency Management 1.1.0
* Project Lombok
* Spring Boot Starter
* Spring Boot Starter Web
* Spring Boot Starter Test
* MapStruct 1.5.3.Final

## Usage

Use a tool like API testing tool like Postman to send HTTP GET requests to the following endpoint:
GET http://localhost:8080/v1/github/repositories/{userName}

Replace {userName} with the desired GitHub username for which you want to list the repositories.
Include the Accept header in your request to specify the desired response format. The supported media type is application/json.

If you specify the Accept header as application/xml, the API will throw an UnsupportedMediaType exception, indicating that the requested media type is not supported.
Ensure that the specified GitHub username exists. If the user is not found, the API will throw a UserNotFoundException exception.