/**
 * Контроллер для управления заказами (Order).
 * 
 * Назначение:
 * - Обрабатывает HTTP-запросы, связанные с созданием, получением, отменой, оплатой и возвратом заказов.
 * - Взаимодействует с OrderService, OrderCostService и SoldOrderService для выполнения бизнес-логики.
 * 
 * Принимает и возвращает DTO:
 * - OrderDto, OrderCreateResponseDto, OrderCancelDto, CostCalculationDto, CostResponseDto, SoldOrderRequestDto, SoldOrderResponseDto, OrderRefundDto, OrderResponseDto, SimpleOrderRequestDto, EditableOrderRequestDto и др.
 * 
 * Основные методы:
 * - getOrder: Получить заказ по ID
 * - getOrdersByDateRange: Получить заказы за период
 * - createSimpleOrder / createEditableOrder: Создать заказ (разные режимы)
 * - cancelOrder: Отмена заказа
 * - calculateOrderCost: Расчёт стоимости заказа
 * - processSoldOrder: Оплата заказа
 * - refundOrder: Возврат по заказу
 */
package com.ticket.terminal.controller;

import com.ticket.terminal.dto.cost.CostCalculationDto;
import com.ticket.terminal.dto.cost.CostResponseDto;
import com.ticket.terminal.dto.editable.EditableOrderRequestDto;
import com.ticket.terminal.dto.order.*;
import com.ticket.terminal.dto.simple.SimpleOrderRequestDto;
import com.ticket.terminal.dto.sold.SoldOrderRequestDto;
import com.ticket.terminal.dto.sold.SoldOrderResponseDto;
import com.ticket.terminal.service.OrderCostService;
import com.ticket.terminal.service.OrderService;
import com.ticket.terminal.service.SoldOrderService;
import com.ticket.terminal.service.SoldRequestEnricherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("Order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final OrderCostService orderCostService;
    private final SoldOrderService soldOrderService;
    private final SoldRequestEnricherService soldRequestEnricherService;


    @Operation(summary = "Получение заказа по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID заказа"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @Operation(summary = "Получение заказов по диапазону дат")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неверный формат даты"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/Range")
    public ResponseEntity<OrderResponseDto> getOrdersByDateRange(
            @RequestParam(name = "dtBegin") String dtBegin,
            @RequestParam(name = "dtEnd") String dtEnd
    ) {
        return ResponseEntity.ok(orderService.getOrderByDateRange(dtBegin, dtEnd));
    }

    @Operation(summary = "Создание заказа (simple mode)",
            description = "Создание заказа из простых услуг (IsSimpleMode=1)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заказ успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка в теле запроса или отсутствующие данные"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/Create")
    public ResponseEntity<OrderCreateResponseDto> createSimpleOrder(@RequestBody SimpleOrderRequestDto requestDto) {
        return ResponseEntity.ok(orderService.createSimpleOrder(requestDto));
    }

    @Operation(summary = "Создание заказа (editable mode)",
            description = "Создание заказа из редактируемых услуг (IsSimpleMode=0)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заказ успешно создан (редактируемые услуги)"),
            @ApiResponse(responseCode = "400", description = "Ошибка в теле запроса или отсутствующие данные"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/CreateEditable")
    public ResponseEntity<OrderCreateResponseDto> createEditableOrder(@RequestBody EditableOrderRequestDto requestDto) {
        return ResponseEntity.ok(orderService.createEditableOrder(requestDto));
    }

    @Operation(summary = "Отмена заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заказ успешно отменён"),
            @ApiResponse(responseCode = "400", description = "Невалидный формат запроса или ID"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/Cancel")
    public ResponseEntity<OrderDto> cancelOrder(@RequestBody OrderCancelDto requestDto) {
        return ResponseEntity.ok(orderService.cancelOrder(requestDto));
    }

    @Operation(summary = "Расчет стоимости заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Стоимость успешно рассчитана"),
            @ApiResponse(responseCode = "400", description = "Невалидные входные данные"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/Cost")
    public ResponseEntity<CostResponseDto> calculateOrderCost(@RequestBody CostCalculationDto requestDto) {
        return ResponseEntity.ok(orderCostService.calculateCost(requestDto));
    }

    @Operation (summary = "Оплата заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продажа успешно обработана"),
            @ApiResponse(responseCode = "400", description = "Некорректный формат запроса"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/Sold")
    public ResponseEntity<SoldOrderResponseDto> processSoldOrder(@RequestBody SoldOrderRequestDto dto) {
        soldRequestEnricherService.enrich(dto);
        return ResponseEntity.ok(soldOrderService.processSoldOrder(dto));
    }

    @Operation(summary = "Возврат по заказу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возврат успешно выполнен"),
            @ApiResponse(responseCode = "400", description = "Ошибка при возврате, неверные параметры"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/Refund")
    public ResponseEntity<OrderDto> refundOrder(@RequestBody OrderRefundDto dto) {
        return ResponseEntity.ok(orderService.refundOrder(dto));
    }
}
