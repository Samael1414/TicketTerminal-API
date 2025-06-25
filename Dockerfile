FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Копируем файлы проекта
COPY pom.xml .
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# Создаем финальный образ
FROM eclipse-temurin:17-jre
WORKDIR /app

# Копируем собранный JAR из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

# Определяем переменные окружения
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/ticket_terminal
ENV SPRING_DATASOURCE_USERNAME=admin
ENV SPRING_DATASOURCE_PASSWORD=pass

# Открываем порт
EXPOSE 8181

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
