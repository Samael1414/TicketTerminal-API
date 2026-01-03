# TicketTerminal-API

## Описание

**TicketTerminal-API** — система управления услугами и билетами (backend + frontend), разворачиваемая локально через Docker.
Проект предназначен для запуска **без ручных команд**, с помощью готового `start.bat`.

В составе:

* Backend: Spring Boot API
* Frontend: админ-панель (через nginx)
* Database: PostgreSQL
* Миграции: Liquibase

---

## Системные требования

Для запуска **ничего, кроме Docker, не требуется**.

### Обязательно

* **Docker Desktop** (Windows / macOS)

  * Docker должен быть **запущен** (статус *Running*)

### Проверка (по желанию)

```bash
docker --version
docker compose version
```

---

## Быстрый запуск (рекомендуемый способ)

### 1. Скачать проект

* Скачать архив проекта **или** клонировать репозиторий
* Распаковать в любую папку (например `C:\TicketTerminal`)

### 2. Запустить систему

* Дважды кликнуть по файлу:

```
start.bat
```

### 3. Дождаться запуска

* Скрипт автоматически:

  * запустит Docker-контейнеры
  * дождётся готовности backend
  * **сам откроет браузер** с админ-панелью

### 4. Открытие системы

Админ-панель будет доступна по адресу:

```
http://localhost/
```

---

## Остановка системы

Для корректной остановки контейнеров:

```
stop.bat
```

---

## Доступы и API

### Backend API

```
http://localhost:8181/TLMuseumGate/REST
```

### Swagger UI

```
http://localhost:8181/TLMuseumGate/REST/swagger-ui.html
```

---

## Структура проекта

```
TicketTerminal-API
│
├─ frontend/              # frontend (dist используется nginx)
├─ nginx/                 # конфигурация nginx
│   └─ nginx.conf
│
├─ src/
│   └─ main/
│       ├─ java/          # backend (Spring Boot)
│       └─ resources/
│           ├─ db-changelog/   # Liquibase миграции
│           └─ application.yml
│
├─ docker-compose.yml     # запуск всей системы
├─ Dockerfile             # сборка backend
│
├─ start.bat              # запуск системы (для заказчика)
├─ stop.bat               # остановка системы
│
├─ README.md
└─ pom.xml
```

---

## Используемые технологии

* Java 17
* Spring Boot 3.4.3
* Spring Data JPA
* Spring Security
* PostgreSQL 15
* Liquibase
* MapStruct
* Swagger / OpenAPI
* Docker / Docker Compose
* Nginx

---

## Примечания

* Первый запуск может занять **до 1–2 минут** (инициализация БД и контейнеров)
* Все данные БД хранятся в Docker volume
* Для полного сброса данных можно выполнить:

  ```bash
  docker compose down -v
  ```
