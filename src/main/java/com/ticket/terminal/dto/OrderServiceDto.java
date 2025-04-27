package com.ticket.terminal.dto;
/*
POST /REST/Order/Create
GET /REST/Order?OrderId={id}
POST /REST/Order/Sold
POST /REST/Order/Cancel
POST /REST/Order/Refund
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

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
    private Integer serviceId;

    @JsonProperty("ServiceName")
    private String serviceName;

    @JsonProperty("Cost")
    private Integer cost;

    @JsonProperty("dtVisit")
    @NotNull(message = "DtVisit is required for this service")
    private LocalDateTime dtVisit;

    @JsonProperty("ServiceCount")
    private Integer serviceCount;

    @JsonProperty("DtDrop")
    private OffsetDateTime dtDrop;

}
