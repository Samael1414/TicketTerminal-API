/**
 * Сущность (Entity) для хранения информации о заказах (Order) в базе данных.
 * 
 * Назначение:
 * - Представляет таблицу заказов, хранит все основные сведения о заказе.
 * - Используется в сервисах, репозиториях, для построения отчётов и бизнес-логики.
 * 
 * Содержит данные:
 * - id заказа, пользователь, статус, связанные услуги, суммы, даты создания/обновления и др.
 */
package com.ticket.terminal.entity;

import com.ticket.terminal.enums.OrderStatus;
import com.ticket.terminal.enums.ServiceState;
import com.ticket.terminal.util.BarcodeGeneratorUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NotFound;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Slf4j
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
/**
 * Класс-сущность для хранения информации о заказах (Order) в базе данных.
 * Используется для отображения таблицы "orders" в базе данных.
 */
public class OrderEntity {

        /**
     * Уникальный идентификатор заказа (Primary Key).
     * Тип: Long (объект-обёртка для long, может быть null до сохранения в БД).
     * Генерируется автоматически стратегией IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        /**
     * Уникальный штрихкод заказа.
     * Тип: String (строка).
     * Не может быть null, должен быть уникальным.
     */
    @Column(name = "order_barcode", nullable = false, unique = true)
    private String orderBarcode;

        /**
     * Идентификатор состояния заказа (например, "создан", "оплачен").
     * Тип: Integer (объект-обёртка для int, может быть null до установки).
     * Не может быть null.
     */
    @Column(nullable = false)
    private Integer orderStateId;

        /**
     * Статус заказа (enum OrderStatus).
     * Тип: OrderStatus (enum, хранится в БД как строка).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

        /**
     * Пользователь, оформивший заказ.
     * Тип: UsersEntity (сущность пользователя).
     * Связь: многие заказы к одному пользователю (ManyToOne), лениво загружается.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity user;

        /**
     * Данные посетителя (имя, телефон, email).
     * Тип: String (строка), может быть null.
     */
    private String visitorName1;
    private String visitorName2;
    private String visitorName3;
    private String visitorPhone;
    private String visitorMail;

        /**
     * Дата и время создания заказа.
     * Тип: LocalDateTime (дата и время без часового пояса).
     * Устанавливается автоматически при создании, не изменяется.
     */
    @CreatedDate
    @Column(name = "created", updatable = false)
    private LocalDateTime created;

        /**
     * Дата и время последнего изменения заказа.
     * Тип: LocalDateTime.
     * Устанавливается автоматически при каждом обновлении.
     */
    @LastModifiedDate
    @Column(name = "updated")
    private LocalDateTime updated;

        /**
     * Список услуг, связанных с заказом.
     * Тип: List<OrderServiceEntity> (список объектов OrderServiceEntity).
     * Связь: один заказ — много услуг (OneToMany), лениво загружается.
     * mappedBy = "order" — связь по полю order в OrderServiceEntity.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderServiceEntity> service;

        /**
     * Флаг: простой режим оформления заказа (например, для упрощённого интерфейса).
     * Тип: Boolean (объект-обёртка для boolean, может быть null).
     * Не может быть null.
     */
    @Column(name = "is_simple_mode", nullable = false)
    private Boolean isSimpleMode;

        /**
     * Дополнительные данные о посетителе и заказе.
     * Типы: String для текстовых данных, Double для стоимости, LocalDateTime для даты.
     * Все поля могут быть null, если не заполнены.
     */
    private String visitorAddress;
    private String visitorDocumentName;
    private String visitorDocumentSerial;
    private String visitorDocumentNumber;
    private String orderSiteId;
    private String visitorSiteId;
    private String comment;
    private Double cost;
    private LocalDateTime dtDrop;

        /**
     * Внутренний идентификатор заказа (например, для интеграции с внешними системами).
     * Тип: Integer (объект-обёртка для int).
     * Не может быть null.
     */
    @Column(name = "order_id", nullable = false)
    private Integer orderId;

        /**
     * Метод обратного вызова JPA, вызывается перед сохранением сущности в БД.
     * Не принимает параметров, не возвращает значения (void).
     * Устанавливает значения по умолчанию для некоторых полей, если они не заданы.
     */
    @PrePersist
    public void prePersist() {
        if (this.orderBarcode == null || this.orderBarcode.isEmpty()) {
            this.orderBarcode = BarcodeGeneratorUtil.generateOrderBarcode();
        }
        if (this.orderStateId == null) {
            this.orderStateId = ServiceState.ORDERED.getCode();
        }
        if (this.orderStatus == null) {
            this.orderStatus = OrderStatus.NEW;
        }
    }

        /**
     * Устанавливает штрихкод заказа.
     * @param orderBarcode — новый штрихкод (тип String)
     * Не возвращает значения (void).
     */
    public void setOrderBarcode(String orderBarcode) {
        log.error("⚠️ SET orderBarcode → {}", orderBarcode);
        this.orderBarcode = orderBarcode;
    }



}
