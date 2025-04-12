package com.ticket.terminal.util;

public class BarcodeGeneratorUtil {

    public static String generateOrderBarcode() {
        return String.format("9000%d", System.currentTimeMillis());
    }

    public static String generateSoldServiceBarcode(Long orderServiceId) {
        return String.format("9000%d", 300000 + orderServiceId);
    }
}
