-- Сброс и обновление последовательности для таблицы action_logs
SELECT setval('action_logs_id_seq', (SELECT MAX(id) FROM action_logs), true);
