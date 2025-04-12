package com.ticket.terminal.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceState {
    ISSUED(1, "Выдан"),
    RETURNED(2, "Возвращен"),
    ORDERED(4, "Заказан"),
    PAID(5, "Оплачен");

    private final Integer code;

    private final String name;

    public static String getNameByCode(Integer code) {
        for (ServiceState state : values()) {
            if (state.getCode().equals(code)) {
                return state.getName();
            }
        }
        return null;
    }
}
