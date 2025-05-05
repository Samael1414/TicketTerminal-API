package com.ticket.terminal.entity;

import com.ticket.terminal.enums.OrderStatus;
import com.ticket.terminal.enums.ServiceState;
import com.ticket.terminal.util.BarcodeGeneratorUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class OrderEntityUtil {

    private OrderEntityUtil() {
    }

    public static void initialize(Object obj) {
        if (!(obj instanceof OrderEntity orderEntity)) return;

        log.warn("⚠️ Инициализация OrderEntity. Был orderBarcode: {}", orderEntity.getOrderBarcode());

        if (orderEntity.getOrderBarcode() == null) {
            orderEntity.setOrderBarcode(BarcodeGeneratorUtil.generateOrderBarcode());
            log.warn("🔥 Установлен новый orderBarcode: {}", orderEntity.getOrderBarcode());
        }

        if (orderEntity.getOrderStateId() == null) {
            orderEntity.setOrderStateId(ServiceState.ORDERED.getCode());
        }

        if (orderEntity.getOrderStatus() == null) {
            orderEntity.setOrderStatus(OrderStatus.NEW);
        }

        if (orderEntity.getOrderBarcode() == null) {
            throw new IllegalStateException("❌ orderBarcode всё ещё NULL перед сохранением!");
        }
    }

}
