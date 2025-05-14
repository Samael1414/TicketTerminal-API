/**
 * Кастомный десериализатор для ZonedDateTime.
 *
 * Используется Jackson для преобразования строкового представления даты и времени в объект ZonedDateTime
 * с использованием формата 'yyyy-MM-dd HH:mm'.
 * Применяется при обработке JSON, содержащих даты.
 */
package com.ticket.terminal.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class CustomZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public ZonedDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser.getText().trim();
        LocalDateTime localDateTime = LocalDateTime.parse(value, FORMATTER);
        return localDateTime.atZone(ZoneId.systemDefault());
    }
}
