-- Добавление поля is_root в таблицу пользователей
ALTER TABLE users ADD COLUMN IF NOT EXISTS is_root BOOLEAN NOT NULL DEFAULT FALSE;

-- Создание таблицы прав доступа пользователей
CREATE TABLE IF NOT EXISTS user_permissions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    can_manage_users BOOLEAN NOT NULL DEFAULT FALSE,
    can_manage_services BOOLEAN NOT NULL DEFAULT FALSE,
    can_manage_categories BOOLEAN NOT NULL DEFAULT FALSE,
    can_manage_visit_objects BOOLEAN NOT NULL DEFAULT FALSE,
    can_view_reports BOOLEAN NOT NULL DEFAULT FALSE,
    can_manage_settings BOOLEAN NOT NULL DEFAULT FALSE,
    can_manage_orders BOOLEAN NOT NULL DEFAULT FALSE,
    can_export_data BOOLEAN NOT NULL DEFAULT FALSE,
    can_import_data BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_user_permissions_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Создание индекса для быстрого поиска прав доступа по пользователю
CREATE INDEX IF NOT EXISTS idx_user_permissions_user_id ON user_permissions(user_id);

-- Создание root-пользователя, если он не существует
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM users WHERE user_name = 'root') THEN
        -- Пароль: 'root123' - закодированный с помощью BCrypt
        INSERT INTO users (user_name, password, role, full_name, created_at, is_root)
        VALUES ('root', '$2a$10$8K1p/a7OqMk7LJ7REHYym.WlxH5U1jFWRk7ZUjA/6s9H0qj3dCnAO', 'ADMIN', 'Root Administrator', NOW(), TRUE);
        
        -- Получаем ID созданного root-пользователя
        WITH root_user AS (SELECT id FROM users WHERE user_name = 'root' LIMIT 1)
        INSERT INTO user_permissions (
            user_id, 
            can_manage_users, 
            can_manage_services, 
            can_manage_categories, 
            can_manage_visit_objects, 
            can_view_reports, 
            can_manage_settings, 
            can_manage_orders, 
            can_export_data, 
            can_import_data
        )
        SELECT 
            id, 
            TRUE, 
            TRUE, 
            TRUE, 
            TRUE, 
            TRUE, 
            TRUE, 
            TRUE, 
            TRUE, 
            TRUE
        FROM root_user;
    ELSE
        -- Обновляем существующего root-пользователя
        UPDATE users SET is_root = TRUE WHERE user_name = 'root';
        
        -- Получаем ID существующего root-пользователя
        WITH root_user AS (SELECT id FROM users WHERE user_name = 'root' LIMIT 1)
        INSERT INTO user_permissions (
            user_id, 
            can_manage_users, 
            can_manage_services, 
            can_manage_categories, 
            can_manage_visit_objects, 
            can_view_reports, 
            can_manage_settings, 
            can_manage_orders, 
            can_export_data, 
            can_import_data
        )
        SELECT 
            id, 
            TRUE, 
            TRUE, 
            TRUE, 
            TRUE, 
            TRUE, 
            TRUE, 
            TRUE, 
            TRUE, 
            TRUE
        FROM root_user
        WHERE NOT EXISTS (
            SELECT 1 FROM user_permissions p 
            JOIN root_user r ON p.user_id = r.id
        );
    END IF;
END
$$;
