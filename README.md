# TicketTerminal-API

## Описание
API для системы управления билетами и услугами через терминал.

## Требования
- Docker
- Docker Compose

## Сборка и запуск в Docker

### Запуск всего приложения с базой данных
```bash
docker-compose up -d
```

Это команда запустит:
1. PostgreSQL базу данных на порту 5434
2. API приложение на порту 8181

### Доступ к API
После запуска API будет доступно по адресу:
```
http://localhost:8181/TLMuseumGate/REST
```

Swagger UI доступен по адресу:
```
http://localhost:8181/TLMuseumGate/REST/swagger-ui.html
```

### Остановка контейнеров
```bash
docker-compose down
```

### Остановка контейнеров с удалением данных
```bash
docker-compose down -v
```

## Структура проекта
- `src/` - исходный код приложения
- `src/main/resources/db-changelog/` - миграции базы данных (Liquibase)
- `Dockerfile` - инструкции для сборки Docker образа
- `docker-compose.yml` - конфигурация для запуска приложения и базы данных

## Технологии
- Java 17
- Spring Boot 3.4.3
- Spring Data JPA
- PostgreSQL
- Liquibase
- MapStruct
- Swagger/OpenAPI
- Docker
