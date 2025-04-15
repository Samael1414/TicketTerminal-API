package com.ticket.terminal.controller;

import com.ticket.terminal.dto.*;
import com.ticket.terminal.service.OrderCostService;
import com.ticket.terminal.service.OrderService;
import com.ticket.terminal.service.SoldOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/TLMuseumGate/REST")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderCostService orderCostService;
    private final SoldOrderService soldOrderService;


    @Operation(summary = "Получение заказа по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID заказа"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/Order/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("orderId") Long orderId) {
        OrderDto orderDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDto);
    }

    @Operation(summary = "Получение заказов по диапазону дат")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Неверный формат даты"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/Order/Range")
    public ResponseEntity<OrderResponseDto> getOrdersByDateRange(
            @RequestParam(name = "dtBegin") String dtBegin,
            @RequestParam(name = "dtEnd") String dtEnd
    ) {
        //TODO: перенеси всю логику на уровень сервиса. Тоесть return ResponseEntity.ok(orderService.getOrderByDateRange(dtBegin, dtEnd)); а уже в сервисе парсишь из стринги в дату и время и делаешь с ними что нужно
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime begin = LocalDate.parse(dtBegin, formatter).atStartOfDay();
        LocalDateTime end = LocalDate.parse(dtEnd, formatter).plusDays(1).atStartOfDay().minusNanos(1);
        OrderResponseDto response = orderService.getOrderByDateRange(begin, end);
        return ResponseEntity.ok(response);
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
    @PostMapping("/Order/Create")
    public ResponseEntity<OrderCreateResponseDto> createSimpleOrder(@RequestBody SimpleOrderRequestDto requestDto) {
        //TODO: return ResponseEntity.ok(orderService.createSimpleOrder(requestDto)); не надо создавать кучу не нужных переменных, передавай в OK сразу вызов метода сервиса. ПРОСМОТРИ ВСЕ МЕТОДЫ  КОНТРОЛЛЕРОВ НА НАЛИЧИЕ ТАКИХ МОМЕНТОВ И ПОПРАВЬ
        OrderCreateResponseDto responseDto = orderService.createSimpleOrder(requestDto);
        return ResponseEntity.ok(responseDto);
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
    @PostMapping("/Order/CreateEditable")
    public ResponseEntity<OrderCreateResponseDto> createEditableOrder(@RequestBody EditableOrderRequestDto requestDto) {
        OrderCreateResponseDto responseDto = orderService.createEditableOrder(requestDto);
        return ResponseEntity.ok(responseDto);
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
    @PostMapping("/Order/Cancel")
    public ResponseEntity<OrderDto> cancelOrder(@RequestBody OrderCancelDto requestDto) {
        OrderDto orderDto = orderService.cancelOrder(requestDto);
        return ResponseEntity.ok(orderDto);
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
    @PostMapping("/Order/Cost")
    public ResponseEntity<CostResponseDto> calculateOrderCost(@RequestBody CostCalculationDto requestDto) {
        CostResponseDto response = orderCostService.calculateCost(requestDto);
        return ResponseEntity.ok(response);
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
    @PostMapping("/Order/Sold")
    public ResponseEntity<SoldOrderResponseDto> processSoldOrder(@RequestBody SoldOrderRequestDto dto) {
        SoldOrderResponseDto response = soldOrderService.processSoldOrder(dto);
        return ResponseEntity.ok(response);
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
    @PostMapping("/Order/Refund")
    public ResponseEntity<OrderDto> refundOrder(@RequestBody OrderRefundDto dto) {
        OrderDto response = orderService.refundOrder(dto);
        return ResponseEntity.ok(response);
    }
}
