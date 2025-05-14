///**
// * Feign-клиент для взаимодействия с внешней системой MuseumGate.
// *
// * Назначение:
// * - Позволяет получать справочники услуг, создавать заказы (simple и editable), получать расширенные данные об услугах.
// * - Используется для интеграции с внешним REST API музея (TLMuseumGate).
// *
// * Обрабатывает/возвращает DTO:
// * - SimpleServiceDto, EditableOrderRequestDto, SimpleOrderRequestDto, TLMuseumServiceResponseDto, OrderCreateResponseDto и др.
// *
// * Методы:
// * - getSimpleServiceList(): Получить список простых услуг.
// * - createEditableOrder(): Создать заказ с редактируемыми услугами.
// * - createSimpleOrder(): Создать заказ с простыми услугами.
// * - getFullEditableService(): Получить полный список редактируемых услуг.
// */
//package com.ticket.terminal.client;
//
//import com.ticket.terminal.config.FeignClientConfiguration;
//import com.ticket.terminal.dto.*;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import java.util.List;
//
//
//@FeignClient(
//        name = "museumGateClient",
//        url  = "${application.clients.tonline-gate.url}",
//        path = "/TLMuseumGate/REST",
//        configuration = FeignClientConfiguration.class
//)
//public interface MuseumClient {
//
//    /**
//     * Получает список простых услуг из внешней системы.
//     *
//     * @return List<SimpleServiceDto> — список объектов, представляющих простые услуги
//     *         Каждый объект содержит базовую информацию об услуге (ID, название, цена и т.д.)
//     */
//    @GetMapping(
//            value    = "/Service/Simple",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    List<SimpleServiceDto> getSimpleServiceList();
//
//    /**
//     * Создает заказ с редактируемыми услугами во внешней системе.
//     *
//     * @param requestDto — объект запроса на создание заказа с редактируемыми услугами (тип EditableOrderRequestDto)
//     *                    Содержит информацию о заказе, включая список услуг, данные о посетителях и т.д.
//     * @return OrderCreateResponseDto — объект с информацией о созданном заказе
//     *         Включает ID заказа, статус, штрих-код и другие данные созданного заказа
//     */
//    @PostMapping(
//            value    = "/Create",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    OrderCreateResponseDto createEditableOrder(@RequestBody EditableOrderRequestDto requestDto);
//
//    /**
//     * Создает заказ с простыми (нередактируемыми) услугами во внешней системе.
//     *
//     * @param requestDto — объект запроса на создание простого заказа (тип SimpleOrderRequestDto)
//     *                    Содержит базовую информацию о заказе и список простых услуг
//     * @return OrderCreateResponseDto — объект с информацией о созданном заказе
//     *         Включает ID заказа, статус, штрих-код и другие данные созданного заказа
//     */
//    @PostMapping(
//            value    = "/CreateSimple",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    OrderCreateResponseDto createSimpleOrder(@RequestBody SimpleOrderRequestDto requestDto);
//
//    /**
//     * Получает полный список редактируемых услуг из внешней системы.
//     *
//     * @return TLMuseumServiceResponseDto — объект, содержащий полный список редактируемых услуг
//     *         Включает расширенную информацию о каждой услуге, доступной для редактирования
//     */
//    @GetMapping(
//            value = "/Service/FullEditable",
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    TLMuseumServiceResponseDto getFullEditableService();
//
//}
