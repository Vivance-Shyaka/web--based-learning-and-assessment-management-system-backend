# Web-Based Learning And Assessment Management System  -  Mid-Term Project Examination (Building the Backend sytem)




**Name :**  Vivance SHYAKA 

**ID :**  27237 

**Project Name:** Web-Based Learning And Assessment Management System 

**Course:** INSY 8322 WEB TECHNOLOGY AND INTERNET

**Lecturer:**  Patrick DUSHIMIMANA



## What the project is About

This is  backend system for a learning and assessment platform. Leaners make quiz attempts, an instructor (who runs one course and teaches several topics) grade them, and after an attempt is graded the instructor writes result fedback with marks for each question area. A quiz attempt moves from `STARTED` to `SUBMITTED` and ends as `GRADED` or `ABANDONED`.

## Technologies Used

- Java 21 
- Spring Boot 4.1.2 (Spring Web, Spring Data JPA with Hibernate)
- PostgreSQL
- Maven 
- Postman(for testing)



## How to run the My Backend system(API)

**1. Clone the repository**

```
git clone https://github.com/Vivance-Shyaka/web--based-learning-and-assessment-management-system-backend.git
```

**2. Create the database**

Tables are created automatically, but the database itself must exist. In `psql` or pgAdmin:

```sql
CREATE DATABASE wblam_db;
```

**3. Create `src/main/resources/application.properties`**

 Create your own property with this content and put your own PostgreSQL password and usernmae:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/wblam_db
spring.datasource.username=YOUR_POSTGRES_USERNAME
spring.datasource.password=YOUR_POSTGRES_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8087(you can use port that you want)
```

**4. Start the application**

```
mvn spring-boot:run
```


## Postman collection

The file `web--based-learning-and-assessment-management-system-midterm.postman_collection.json` is in the root of this repository. In Postman, choose **Import** and select it. It contains one request per requirement but if you have charged your port , you can replace it with your own 

## My Project structure

```
src/main/java/kigali/learning/rw
├── WebBasedLearningAndAssessmentManagementSystemApplication.java
├── domain        entities and the AttemptStatus enum
├── repository    Spring Data JPA repositories (derived queries and JPQL)
├── service       business rules, return a List, an Optional or a String message
└── controller    REST endpoints, map the message to an HTTP status
```


## <center>Thank you for reviewing my work.!</center>
