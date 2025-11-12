# Spring Data Specification

A Spring Boot REST API application for managing articles with advanced search and filtering capabilities using JPA Specifications.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [API Endpoints](#api-endpoints)
- [Query Parameters](#query-parameters)
- [Project Structure](#project-structure)
- [Database](#database)
- [Examples](#examples)

## 🎯 Overview
This project demonstrates a RESTful API built with Spring Boot that provides article management functionality. It showcases two different approaches to implementing JPA Specifications for dynamic query filtering: Lambda-based inline specifications and Class-based specifications.

## ✨ Features
- **RESTful API** for article management
- **Dynamic Search and Filtering** with multiple criteria:
  - Title (case-insensitive partial match)
  - Author (case-insensitive partial match)
  - Slug (exact match)
  - Publish date range (from/to)
- **Pagination Support** with configurable page size and page number
- **JPA Specifications** - Two implementation approaches:
  - Lambda-based inline specification
  - Class-based specification with criteria object
- **In-memory H2 Database** for easy development and testing
- **Auto-initialization** with sample data on startup
- **Bean Validation** for request parameters

## 📡 API Endpoints
### Get Articles

Retrieves articles with optional filtering and pagination. Returns results using both Lambda and Class-based specification approaches.

**Endpoint**: `GET /api/v1/article`

**Response**: Returns a JSON object with two keys:
- `allWithLambda`: Results using lambda-based specification
- `allWithClass`: Results using class-based specification

## 🔍 Query Parameters

| Parameter | Type | Required | Default | Constraints | Description |
|-----------|------|----------|---------|-------------|-------------|
| `next` | Integer | No | 0 | Min: 0, Max: 99 | Page number (zero-based) |
| `max` | Integer | No | 10 | Min: 1, Max: 99 | Number of items per page |
| `title` | String | No | - | - | Filter by article title (case-insensitive, partial match) |
| `author` | String | No | - | - | Filter by author name (case-insensitive, partial match) |
| `slug` | String | No | - | - | Filter by slug (exact match) |
| `publishFrom` | String | No | - | ISO 8601 format | Filter articles published from this date |
| `publishTo` | String | No | - | ISO 8601 format | Filter articles published until this date |

## 📁 Project Structure

```
spring-data-specification/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/github/senocak/
│   │   │       ├── ArticleApplication.java          # Main application class & REST controller
│   │   │       └── domain/
│   │   │           ├── Article.java                 # JPA Entity
│   │   │           ├── ArticleRepository.java       # JPA Repository
│   │   │           ├── ArticleSpecification.java    # Class-based Specification
│   │   │           └── ArticleCriteria.java         # Criteria DTO
│   │   └── resources/
│   │       ├── application.yml                      # Application configuration
│   │       └── requests.http                        # HTTP request examples
├── pom.xml                                          # Maven configuration
└── README.md                                        # Project documentation
```

## 💾 Database

### Article Entity

The `Article` entity contains the following fields:

| Field | Type | Description |
|-------|------|-------------|
| `id` | String (UUID) | Primary key |
| `title` | String (max 100 chars) | Article title |
| `slug` | String | URL-friendly identifier |
| `content` | String (LOB) | Article content |
| `author` | String | Author name |
| `publish` | String | Publication date (ISO 8601) |
| `createdAt` | Date | Auto-generated creation timestamp |
| `updatedAt` | Date | Auto-generated update timestamp |

### Sample Data

On application startup, 10 sample articles are automatically created with:
- Titles: "Article Title 1" through "Article Title 10"
- Slugs: "article-title-1" through "article-title-10"
- Authors: "Article Author 1" through "Article Author 10"
- Publish date: "2010-01-01T12:00:00+01:00"

## 📝 Examples

### Get All Articles (Default Pagination)

```bash
curl http://localhost:8080/api/v1/article
```

### Get Articles with Custom Pagination

```bash
curl "http://localhost:8080/api/v1/article?next=0&max=5"
```

### Search by Title

```bash
curl "http://localhost:8080/api/v1/article?title=article"
```

### Search by Author

```bash
curl "http://localhost:8080/api/v1/article?author=author%202"
```

### Search by Slug

```bash
curl "http://localhost:8080/api/v1/article?slug=article-title-2"
```

### Filter by Date Range

```bash
curl "http://localhost:8080/api/v1/article?publishFrom=2009-01-01&publishTo=2019-01-01"
```

### Combined Search

```bash
curl "http://localhost:8080/api/v1/article?next=0&max=5&title=2&author=2&slug=article-title-2&publishFrom=2009-01-01&publishTo=2019-01-01"
```

### Example Response

```json
{
  "allWithLambda": {
    "content": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "title": "Article Title 2",
        "slug": "article-title-2",
        "content": "Article Content 2",
        "author": "Article Author 2",
        "publish": "2010-01-01T12:00:00+01:00",
        "createdAt": "2025-11-12T10:30:00",
        "updatedAt": "2025-11-12T10:30:00"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 5,
      "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 1,
    "totalElements": 1,
    "last": true,
    "size": 5,
    "number": 0,
    "numberOfElements": 1,
    "first": true,
    "empty": false
  },
  "allWithClass": {
    // ... same structure as allWithLambda
  }
}
```

## 🏗 Architecture Highlights

### Two Specification Approaches

This project demonstrates two ways to implement JPA Specifications:

#### 1. Lambda-Based (Inline Specification)
```java
Specification<Article> specification = (root, query, criteriaBuilder) -> {
    // Build predicates inline
};
```

#### 2. Class-Based (ArticleSpecification)
```java
public class ArticleSpecification implements Specification<Article> {
    // Encapsulated specification logic with criteria object
}
```

Both approaches produce identical results, demonstrating the flexibility of Spring Data JPA Specifications.
