# AI-Powered Career Guidance Platform — Backend

A Spring Boot 3 backend for an AI-powered career guidance platform that personalizes learning roadmaps, weekly preparation plans, skills quizzes, and industry insights for students and early-career professionals. It integrates authentication (email/password + Google OAuth), JWT-based security, email notifications, PostgreSQL persistence, and Google Gemini for AI-generated content.

## Key Features

- Authentication & Authorization
  - Email/password registration and login (`AuthController`)
  - Google OAuth sign-in using ID token
  - JWT-based access/refresh tokens, token refresh and logout
  - Email verification, forgot/reset password via SMTP
- Onboarding Profile
  - Persist detailed user onboarding data (degree/branch, skills, goals, target companies, schedule)
- AI-Powered Personalization
  - Personalized technical quiz generation and scoring
  - AI-generated multi-milestone career roadmap (`RoadmapController`/`RoadmapService`)
  - Week-wise preparation plan from the roadmap
  - Industry insights including salary ranges, growth rate, demand level, top skills and key trends
- Roadmap & Weekly Plan Management
  - Create, retrieve, update, delete roadmaps
  - Generate week-wise plan aligned to roadmap modules
- Developer Experience
  - OpenAPI/Swagger UI at `/swagger-ui.html`
  - Postman Testing Guide included at `POSTMAN_TESTING_GUIDE.md`
  - Environment-based configuration with `.env` via `spring-dotenv`

## Tech Stack

- Java 21, Spring Boot 3.5.x
- Spring Web, Spring Data JPA, Spring Security, OAuth2 Client
- PostgreSQL, Hibernate
- JWT (jjwt)
- Mail (Spring Mail)
- OpenAPI/Swagger (`springdoc-openapi`)
- Google Gemini API Integration (via `RestClient`)

## Project Structure (Backend)

```
src/main/java/com/career/guide/backend/
  config/               # Security, OpenAPI, mapper, properties (JWT, Gemini, Google OAuth)
  controller/           # REST controllers (Auth, Onboarding, Roadmap, Quiz, Industry, WeeklyPlan, User)
  dto/                  # Request/Response DTOs (auth, roadmap, quiz, onboarding, industry, etc.)
  entity/               # JPA entities (User, OnboardingData, PersonalizedRoadmap, Quiz, etc.)
  repository/           # Spring Data repositories
  security/             # Security utilities and configuration
  service/              # Business logic services, incl. Gemini integration
  util/                 # Helpers (responses, etc.)
src/main/resources/
  application.properties
  static/
  templates/
```

## API Overview

- Auth (`/api/auth`): register, login, Google OAuth, verify email, refresh token, forgot/reset password, logout
- Onboarding (`/api/onboarding`): CRUD onboarding data
- Roadmaps (`/api/roadmaps`): generate, list, get, update, delete
- Quizzes (`/api/quiz`): generate quiz, submit, get results
- Industry Insights (`/api/industry`): fetch AI-generated insights
- Weekly Plan (`/api/weekly-plans`): generate/fetch week plans aligned to roadmap
- Users (`/api/users`): profile endpoints

OpenAPI/Swagger UI: `/swagger-ui.html`
OpenAPI JSON: `/v3/api-docs`

See `POSTMAN_TESTING_GUIDE.md` for request/response examples of common flows.

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- PostgreSQL 14+

### Clone & Enter Project

```
# backend is this module; repository root may include other modules
# git clone <your-repo-url>
```

### Configure Environment

This project uses environment variables. You can create a `.env` file at the backend module root to load values automatically (via `spring-dotenv`).

Example `.env` (do not commit real secrets):

```
# --- Security / JWT ---
APP_JWT_SECRET=change-this-to-a-long-random-secret
APP_JWT_EXPIRATION_SECONDS=3600

# --- Database (PostgreSQL) ---
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/career_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# --- Google Gemini API ---
GEMINI_API_KEY=your-gemini-api-key
# Optional overrides
# GEMINI_API_BASE_URL=https://generativelanguage.googleapis.com/v1beta
# GEMINI_API_MODEL=models/gemini-1.5-flash

# --- Google OAuth (Sign-in with Google) ---
GOOGLE_CLIENT_ID=your-google-oauth-client-id
GOOGLE_CLIENT_SECRET=your-google-oauth-client-secret

# --- SMTP (Email) ---
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USERNAME=your-email@example.com
SMTP_PASSWORD=your-email-app-password-or-smtp-password
SMTP_FROM_EMAIL=Career Guide <no-reply@your-domain.com>
```

All variables above map directly to `src/main/resources/application.properties` keys like:

```
app.jwt.secret=${APP_JWT_SECRET}
app.jwt.expiration-seconds=${APP_JWT_EXPIRATION_SECONDS:3600}
gemini.api.key=${GEMINI_API_KEY}
gemini.api.base-url=${GEMINI_API_BASE_URL:https://generativelanguage.googleapis.com/v1beta}
gemini.api.model=${GEMINI_API_MODEL:models/gemini-1.5-flash}

# Google OAuth
google.oauth.client-id=${GOOGLE_CLIENT_ID}
google.oauth.client-secret=${GOOGLE_CLIENT_SECRET}

# Email
spring.mail.host=${SMTP_HOST:smtp.gmail.com}
spring.mail.port=${SMTP_PORT:587}
spring.mail.username=${SMTP_USERNAME}
spring.mail.password=${SMTP_PASSWORD}
app.mail.from=${SMTP_FROM_EMAIL}

# Database
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
```

### Database

- Create a PostgreSQL database (matching your `SPRING_DATASOURCE_URL`):
  - Example: database name `career_db`
- The app uses `spring.jpa.hibernate.ddl-auto=update` to auto-manage schema during development.

### Run the Application

Using Maven Wrapper:

```
./mvnw spring-boot:run
```

Or package and run:

```
./mvnw clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

Once running:

- Base URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

### Testing With Postman

Use the step-by-step guide in `POSTMAN_TESTING_GUIDE.md` covering:

- Register → Verify Email → Login → Refresh → Logout
- Create/Update Onboarding
- Generate Roadmap
- Retrieve Roadmaps

You can set Postman environment variables like `baseUrl`, `accessToken`, `refreshToken` for convenience.

## Architecture & Important Modules

- Configuration (`config/`)
  - `SecurityConfig` — Spring Security and JWT setup
  - `JwtProperties` — binds `app.jwt.*` env config
  - `GeminiProperties` — binds `gemini.api.*` env config
  - `OpenApiConfig` — Swagger/OpenAPI configuration
  - `MapperConfig` — shared ModelMapper bean
- Controllers (`controller/`)
  - `AuthController` — auth flows: register, login, Google OAuth, email verify, refresh, logout
  - `RoadmapController` — generate/list/get/update/delete roadmaps
  - `OnboardingController`, `QuizController`, `IndustryInsightController`, `WeeklyPlanController`, `UserController`
- Services (`service/`)
  - `GeminiApiService` — prompts Gemini for quiz questions, roadmaps, insights, weekly plans, improvement tips
  - `AuthService`, `OnboardingService`, `RoadmapService`, `QuizService`, `IndustryInsightService`, `WeeklyPlanService`, `UserService`
- Entities (`entity/`)
  - Core domain: `User`, `UserProfile`, `OnboardingData`, `PersonalizedRoadmap`, `PreparationPlan`, `WeeklyPlan`, `UserProgress`, `UserResume`
  - Quiz domain: `QuizQuestion`, `QuizResult`
  - Insights domain: `IndustryInsight`
  - Security: `RefreshToken`

## Security & Auth Flows

- Registration returns JWT `accessToken` + `refreshToken`.
- Email verification endpoint finalizes account verification.
- Google OAuth: Frontend sends `idToken` to `/api/auth/oauth2/google`.
- Refresh token endpoint issues new access/refresh tokens.
- Use `Authorization: Bearer <accessToken>` for protected endpoints.

## Environment & Secrets

- Do not commit real secrets to the repository.
- Prefer `.env` for local dev and secret managers for production.
- Rotate `APP_JWT_SECRET` when compromised and invalidate refresh tokens on logout.

## Observability

- Spring Boot Actuator is included. You can enable endpoints as needed via properties.

## Common URLs

- Health: `GET /actuator/health`
- Swagger UI: `GET /swagger-ui.html`
- OpenAPI JSON: `GET /v3/api-docs`

## Roadmap (Next Enhancements)

- Add Docker Compose for Postgres + backend
- Role-based admin features and dashboards
- Advanced analytics of learning progress
- Rate limiting and audit logging
- Caching for industry insights

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feat/your-feature`
3. Commit changes: `git commit -m "feat: add your feature"`
4. Push branch: `git push origin feat/your-feature`
5. Open a Pull Request

## License

Specify your license here (e.g., MIT). If you need, I can add a standard `LICENSE` file.
