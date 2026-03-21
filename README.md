# 03 - JWT Auth Service

## Concept

Microservices need stateless authentication because a service cannot keep session state when it may be running across 10 instances. JWT (JSON Web Tokens) solve this problem in a straightforward way.

A user logs in and receives a signed token. That token is sent on every subsequent request. Any service can then validate the token on its own without needing to call back to the Auth Service.

---

## How to Run

```bash
mvn spring-boot:run
```

---

## Testing

```bash
# Login and get a token
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Validate a token
curl http://localhost:8081/auth/validate \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## Demo Users

| Username | Password | Roles |
|----------|----------|-------|
| user | password | USER |
| admin | admin123 | USER, ADMIN |

---

## Java 8 Features Used

| Feature | Where It Is Used |
|---------|-----------------|
| Functional Interface with Function | Generic claims extractor using Function<Claims, T> |
| Optional | Null-safe extraction of Bearer token and claims |
| Stream with map() | Transforming roles to uppercase |
| Method References | Claims::getSubject and String::toUpperCase |
