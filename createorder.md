# Руководство по процессу создания заказа в TicketTerminal-API

## Общий обзор

Процесс создания заказа в системе TicketTerminal-API включает несколько этапов, начиная от инициализации объекта заказа и заканчивая сохранением его в базе данных с соответствующими статусами. В этом руководстве подробно описан весь процесс с акцентом на управление статусами заказа.

## Статусы заказа

В системе существуют два типа статусов:

### 1. OrderStatus (статус заказа в целом)

Определен в перечислении `OrderStatus.java`:

```java
public enum OrderStatus {
    NEW,             // Новый заказ
    CREATED,         // Создан
    FUNDING,         // В процессе оплаты
    PAID,            // Оплачен
    PAID_NOT_SOLD,   // Оплачен, но не продан
    SOLD,            // Продан
    REJECT_PAYMENT   // Отказ от оплаты
}
```

### 2. ServiceState (статус услуги в заказе)

Определен в перечислении `ServiceState.java`:

```java
public enum ServiceState {
    ISSUED(1, "Выдан"),
    RETURNED(2, "Возвращен"),
    ORDERED(4, "Заказан"),
    PAID(5, "Оплачен");

    private final Integer code;
    private final String name;
    
    // Метод для получения названия статуса по коду
    public static String getNameByCode(Integer code) {
        for (ServiceState state : values()) {
            if (state.getCode().equals(code)) {
                return state.getName();
            }
        }
        return null;
    }
}
```

## Процесс создания заказа

### 1. Инициализация заказа

Когда клиент отправляет запрос на создание заказа, система обрабатывает его через один из двух методов:
- `createSimpleOrder` - для создания простого заказа
- `createEditableOrder` - для создания редактируемого заказа

Оба метода следуют схожему процессу:

#### 1.1 Преобразование DTO в Entity

```java
// Для простого заказа
OrderEntity orderEntity = orderMapper.toEntity(requestDto);

// Для редактируемого заказа
OrderEntity orderEntity = orderMapper.toEntity(requestDto);
```

#### 1.2 Инициализация заказа через OrderEntityUtil

```java
OrderEntityUtil.initialize(orderEntity);
```

В методе `initialize` происходит следующее:
- Проверка и генерация штрих-кода заказа
- Установка статуса заказа:
  ```java
  if (orderEntity.getOrderStateId() == null) {
      orderEntity.setOrderStateId(ServiceState.ORDERED.getCode()); // Устанавливается код 4 - "Заказан"
  }
  
  if (orderEntity.getOrderStatus() == null) {
      orderEntity.setOrderStatus(OrderStatus.NEW); // Устанавливается статус NEW
  }
  ```

#### 1.3 Генерация уникального ID и штрих-кода

```java
if (orderEntity.getOrderId() == null) {
    orderEntity.setOrderId(generateUniqueOrderId());
}

if (orderEntity.getOrderBarcode() == null || orderEntity.getOrderBarcode().isEmpty()) {
    orderEntity.setOrderBarcode(BarcodeGeneratorUtil.generateUniqueOrderBarcode(orderRepository));
}
```

### 2. Создание услуг заказа

#### 2.1 Построение списка услуг

Для простого заказа:
```java
List<OrderServiceEntity> orderServiceEntities = buildOrderServices(requestDto, orderEntity);
```

Для редактируемого заказа:
```java
List<OrderServiceEntity> orderServiceEntities = buildEditableOrderServices(requestDto, orderEntity);
```

#### 2.2 Установка статуса для каждой услуги

В методах `buildOrderServices` и `buildEditableOrderServices` для каждой услуги устанавливается статус ORDERED:

```java
orderService.setServiceStateId(ServiceState.ORDERED.getCode()); // Устанавливается код 4 - "Заказан"
```

Это ключевой момент, где каждой услуге в заказе присваивается статус "Заказан" (код 4).

### 3. Сохранение заказа и связанных сущностей

```java
OrderEntity savedOrder = orderRepository.save(orderEntity);
```

После сохранения основной сущности заказа, система сохраняет связанные объекты:
- Объекты посещения (VisitObject)
- Категории посетителей (CategoryVisitor)

### 4. Создание записи в журнале действий

```java
UsersEntity currentUser = getCurrentUser();
actionLogService.save(ActionLogEntity.builder()
        .user(currentUser)
        .actionType("CREATE_ORDER") // или "CREATE_EDITABLE_ORDER"
        .description("Создан заказ № " + savedOrder.getId())
        .createdAt(LocalDateTime.now())
        .actorName(currentUser.getUserName())
        .build());
```

### 5. Формирование и возврат ответа

```java
OrderCreateResponseDto response = orderMapper.toResponseDto(savedOrder);
response.setSoldRequest(soldRequest);
return response;
```

## Жизненный цикл статусов заказа

1. **Создание заказа**:
   - OrderStatus = NEW
   - ServiceState = ORDERED (код 4)

2. **Оплата заказа** (не показано в предоставленном коде):
   - OrderStatus → PAID
   - ServiceState → PAID (код 5)

3. **Выдача билетов/услуг** (не показано в предоставленном коде):
   - ServiceState → ISSUED (код 1)

4. **Возврат** (метод `refundOrder`):
   - ServiceState → RETURNED (код 2)

## Отмена и возврат заказа

### Отмена заказа

Метод `cancelOrder` позволяет отменить весь заказ или отдельные услуги:
- Удаляет связанные записи из таблиц `order_service_visitor` и `order_service_visit_object`
- Удаляет записи услуг из таблицы `order_services`
- Создает запись в журнале действий с типом "CANCEL_ORDER"

### Возврат заказа

Метод `refundOrder` позволяет оформить возврат по заказу:
- Помечает услуги как возвращенные в таблице `sold_services`
- Изменяет статус услуг на RETURNED (код 2)
- Создает запись в журнале действий с типом "REFUND_ORDER"

## Важные моменты

1. Статус ServiceState.ORDERED (код 4) устанавливается в двух местах:
   - При инициализации заказа через OrderEntityUtil.initialize()
   - При создании каждой услуги в методах buildOrderServices и buildEditableOrderServices

2. Статус OrderStatus.NEW устанавливается только при инициализации заказа

3. Статусы заказа и услуг могут изменяться независимо друг от друга

4. Для полного отслеживания изменений статусов необходимо изучить дополнительные сервисы, которые могут изменять эти статусы после создания заказа (например, сервисы оплаты и выдачи билетов)
