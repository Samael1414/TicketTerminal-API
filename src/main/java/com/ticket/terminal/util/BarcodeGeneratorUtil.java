package com.ticket.terminal.util;

import com.ticket.terminal.repository.OrderRepository;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class BarcodeGeneratorUtil {

    /**
     * Генерация уникального orderBarcode, безопасного для высоконагруженных систем.
     */
    public static String generateOrderBarcode() {
        // UUID -> убираем дефисы -> оставляем только цифры -> берем первые 18 символов
        String uuid = UUID.randomUUID().toString().replaceAll("[^0-9]", "");
        if (uuid.length() < 18) {
            uuid = uuid + ThreadLocalRandom.current().nextInt(100000, 999999); // добиваем случайными числами
        }
        return "9000" + uuid.substring(0, 14); // Баркод: 9000 + 14 цифр = 18 символов
    }

    /**
     * Генерация штрихкода проданной услуги.
     * Уникальность гарантируется через уникальность orderServiceId.
     */
    public static String generateSoldServiceBarcode(Long orderServiceId) {
        return String.format("9000%d", 300000 + orderServiceId);
    }

    public static String generateUniqueOrderBarcode(OrderRepository orderRepository) {
        String barcode;
        int attempt = 0;
        do {
            barcode = generateOrderBarcode();
            attempt++;
            if (attempt > 5) {
                throw new IllegalStateException("Не удалось сгенерировать уникальный штрихкод заказа после 5 попыток");
            }
        } while (orderRepository.existsByOrderBarcode(barcode));
        return barcode;
    }
}