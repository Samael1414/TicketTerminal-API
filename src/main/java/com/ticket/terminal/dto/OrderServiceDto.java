/**
 * DTO для передачи данных об услуге, связанной с заказом (Order Service).
 * 
 * Назначение:
 * - Используется для передачи информации об отдельных услугах внутри заказа между слоями приложения и внешними системами.
 * - Применяется в контроллерах, сервисах, клиентах и для обмена с внешними API.
 * 
 * Содержит данные:
 * - id услуги, наименование, стоимость, количество, параметры и др.
 */
package com.ticket.terminal.dto;
/*
POST /REST/Order/Create
GET /REST/Order?OrderId={id}
POST /REST/Order/Sold
POST /REST/Order/Cancel
POST /REST/Order/Refund
 */
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class OrderServiceDto {

    @JsonProperty("OrderServiceId")
    private Long orderServiceId;

    @JsonProperty("ServiceStateId")
    private Integer serviceStateId;

    @JsonProperty("ServiceStateName")
    private String serviceStateName;

    @JsonProperty("ServiceId")
    private Long serviceId;

    @JsonProperty("ServiceName")
    private String serviceName;

    @JsonProperty("Cost")
    private Double cost;

    @JsonAlias("dtVisit")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Moscow")
    private ZonedDateTime dtVisit;

    @JsonProperty("ServiceCount")
    private Integer serviceCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @JsonAlias("dtDrop")
    @JsonProperty("dtDrop")
    private LocalDateTime dtDrop;

}
