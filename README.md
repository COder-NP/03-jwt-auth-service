# 03 — JWT Auth Service

## 📖 Concept
Microservices need stateless authentication — a service can't keep session state
as it may have 10 instances. **JWT (JSON Web Tokens)** solve this:
- User logs in → receives a signed token
- Token is sent on every request
- Any service can validate the token **without calling back** to Auth Service

## 🚀 Run
```bash
mvn spring-boot:run
```

## 🧪 Test
```bash
# Login — get a token
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Validate token
curl http://localhost:8081/auth/validate \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## 👤 Demo Users
| Username | Password | Roles |
|----------|----------|-------|
| user | password | USER |
| admin | admin123 | USER, ADMIN |

## 🧠 Java 8 Features Used
- **Functional Interface** (`Function<Claims, T>`) — generic claims extractor
- **Optional** — null-safe Bearer token & claims extraction
- **Stream + map()** — roles transformation to uppercase
- **Method References** — `Claims::getSubject`, `String::toUpperCase`
