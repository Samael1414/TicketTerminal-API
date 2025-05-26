# Документация по API пользователей

## Общая информация

Система управления пользователями включает в себя следующие возможности:
- Создание, обновление, удаление и получение информации о пользователях
- Управление правами доступа пользователей
- Защита root-пользователей от изменений обычными пользователями

## Эндпоинты

### Получение списка пользователей

```
GET /Users
```

**Требуемые права доступа:** `CAN_MANAGE_USERS` или root-пользователь

**Ответ:**
```json
[
  {
    "Id": 1,
    "UserName": "admin",
    "Role": "ADMIN",
    "FullName": "Администратор",
    "Phone": "+7 (999) 123-45-67",
    "Email": "admin@example.com",
    "CreatedAt": "2023-01-01T12:00:00",
    "IsRoot": true,
    "Permissions": {
      "CanManageUsers": true,
      "CanManageServices": true,
      "CanManageCategories": true,
      "CanManageVisitObjects": true,
      "CanViewReports": true,
      "CanManageSettings": true,
      "CanManageOrders": true,
      "CanExportData": true,
      "CanImportData": true
    }
  }
]
```

### Получение информации о пользователе по ID

```
GET /Users/FindById/{id}
```

**Требуемые права доступа:** `CAN_MANAGE_USERS` или root-пользователь

**Ответ:**
```json
{
  "Id": 1,
  "UserName": "admin",
  "Role": "ADMIN",
  "FullName": "Администратор",
  "Phone": "+7 (999) 123-45-67",
  "Email": "admin@example.com",
  "CreatedAt": "2023-01-01T12:00:00",
  "IsRoot": true,
  "Permissions": {
    "CanManageUsers": true,
    "CanManageServices": true,
    "CanManageCategories": true,
    "CanManageVisitObjects": true,
    "CanViewReports": true,
    "CanManageSettings": true,
    "CanManageOrders": true,
    "CanExportData": true,
    "CanImportData": true
  }
}
```

### Получение информации о текущем пользователе

```
GET /Users/Current
```

**Требуемые права доступа:** Авторизация

**Ответ:**
```json
{
  "Id": 1,
  "UserName": "admin",
  "Role": "ADMIN",
  "FullName": "Администратор",
  "Phone": "+7 (999) 123-45-67",
  "Email": "admin@example.com",
  "CreatedAt": "2023-01-01T12:00:00",
  "IsRoot": true,
  "Permissions": {
    "CanManageUsers": true,
    "CanManageServices": true,
    "CanManageCategories": true,
    "CanManageVisitObjects": true,
    "CanViewReports": true,
    "CanManageSettings": true,
    "CanManageOrders": true,
    "CanExportData": true,
    "CanImportData": true
  }
}
```

### Проверка, является ли текущий пользователь root-пользователем

```
GET /Users/IsRoot
```

**Требуемые права доступа:** Авторизация

**Ответ:**
```json
true
```

### Создание нового пользователя

```
POST /Users/Create
```

**Требуемые права доступа:** `CAN_MANAGE_USERS` или root-пользователь

**Тело запроса:**
```json
{
  "UserName": "newuser",
  "Password": "password123",
  "Role": "USER",
  "FullName": "Новый пользователь",
  "Phone": "+7 (999) 123-45-67",
  "Email": "newuser@example.com",
  "IsRoot": false,
  "Permissions": {
    "CanManageUsers": false,
    "CanManageServices": true,
    "CanManageCategories": true,
    "CanManageVisitObjects": false,
    "CanViewReports": true,
    "CanManageSettings": false,
    "CanManageOrders": true,
    "CanExportData": true,
    "CanImportData": false
  }
}
```

**Ответ:**
```json
{
  "Id": 2,
  "UserName": "newuser",
  "Role": "USER",
  "FullName": "Новый пользователь",
  "Phone": "+7 (999) 123-45-67",
  "Email": "newuser@example.com",
  "CreatedAt": "2023-01-01T12:00:00",
  "IsRoot": false,
  "Permissions": {
    "CanManageUsers": false,
    "CanManageServices": true,
    "CanManageCategories": true,
    "CanManageVisitObjects": false,
    "CanViewReports": true,
    "CanManageSettings": false,
    "CanManageOrders": true,
    "CanExportData": true,
    "CanImportData": false
  }
}
```

### Обновление пользователя

```
PUT /Users/Update/{id}
```

**Требуемые права доступа:** `CAN_MANAGE_USERS` или root-пользователь

**Важно:** Root-пользователи могут быть изменены только другими root-пользователями.

**Тело запроса:**
```json
{
  "UserName": "updateduser",
  "Password": "newpassword123",
  "Role": "MANAGER",
  "FullName": "Обновленный пользователь",
  "Phone": "+7 (999) 123-45-67",
  "Email": "updateduser@example.com",
  "IsRoot": false,
  "Permissions": {
    "CanManageUsers": false,
    "CanManageServices": true,
    "CanManageCategories": true,
    "CanManageVisitObjects": true,
    "CanViewReports": true,
    "CanManageSettings": false,
    "CanManageOrders": true,
    "CanExportData": true,
    "CanImportData": true
  }
}
```

**Ответ:**
```json
{
  "Id": 2,
  "UserName": "updateduser",
  "Role": "MANAGER",
  "FullName": "Обновленный пользователь",
  "Phone": "+7 (999) 123-45-67",
  "Email": "updateduser@example.com",
  "CreatedAt": "2023-01-01T12:00:00",
  "IsRoot": false,
  "Permissions": {
    "CanManageUsers": false,
    "CanManageServices": true,
    "CanManageCategories": true,
    "CanManageVisitObjects": true,
    "CanViewReports": true,
    "CanManageSettings": false,
    "CanManageOrders": true,
    "CanExportData": true,
    "CanImportData": true
  }
}
```

### Удаление пользователя

```
DELETE /Users/Delete/{id}
```

**Требуемые права доступа:** `CAN_MANAGE_USERS` или root-пользователь

**Важно:** Root-пользователи могут быть удалены только другими root-пользователями.

**Ответ:**
```
204 No Content
```

## Эндпоинты для работы с правами доступа

### Получение прав доступа текущего пользователя

```
GET /Permissions/Current
```

**Требуемые права доступа:** Авторизация

**Ответ:**
```json
{
  "CanManageUsers": true,
  "CanManageServices": true,
  "CanManageCategories": true,
  "CanManageVisitObjects": true,
  "CanViewReports": true,
  "CanManageSettings": true,
  "CanManageOrders": true,
  "CanExportData": true,
  "CanImportData": true
}
```

### Получение списка прав доступа текущего пользователя

```
GET /Permissions/List
```

**Требуемые права доступа:** Авторизация

**Ответ:**
```json
{
  "CAN_MANAGE_USERS": true,
  "CAN_MANAGE_SERVICES": true,
  "CAN_MANAGE_CATEGORIES": true,
  "CAN_MANAGE_VISIT_OBJECTS": true,
  "CAN_VIEW_REPORTS": true,
  "CAN_MANAGE_SETTINGS": true,
  "CAN_MANAGE_ORDERS": true,
  "CAN_EXPORT_DATA": true,
  "CAN_IMPORT_DATA": true,
  "ROOT": true
}
```

## Важные замечания

1. При создании и обновлении пользователя всегда указывайте объект `Permissions` с соответствующими правами доступа.
2. Если пользователь является root-пользователем (`IsRoot: true`), то все права доступа будут автоматически установлены в `true`.
3. Только root-пользователи могут создавать других root-пользователей.
4. Только root-пользователи могут изменять или удалять других root-пользователей.
5. При обновлении пользователя не обязательно указывать все поля, можно указать только те, которые нужно изменить.
