INSERT INTO projects (name, customer, start_date, end_date) VALUES
    ('CRM для банка', 'ОАО "Банк"', '2025-01-05', '2025-12-30');

INSERT INTO projects (name, customer, start_date, end_date) VALUES
    ('Мобильное приложение доставки', 'ООО "Быстрая доставка"', '2026-03-01', NULL);

INSERT INTO projects (name, customer, start_date, end_date) VALUES
    ('Интернет-магазин электроники', 'ИП Сидоров', '2026-08-15', NULL);

INSERT INTO programmers
(project_id, last_name, first_name, middle_name, position,
 work_start_date, work_end_date, hourly_rate, is_full_time) VALUES
    (1, 'Иванов',   'Иван',    'Иванович',   'Senior разработчик',  '2025-01-05', '2025-12-30', 25.00, TRUE),
    (1, 'Петрова',  'Мария',   'Сергеевна',  'Middle разработчик',  '2025-02-01', '2025-11-30', 18.00, FALSE),
    (1, 'Сидоров',  'Алексей', 'Викторович', 'Team Lead',           '2025-01-05', '2025-12-30', 30.00, TRUE);

INSERT INTO programmers
(project_id, last_name, first_name, middle_name, position,
 work_start_date, work_end_date, hourly_rate, is_full_time) VALUES
    (2, 'Кузнецова', 'Ольга',    'Андреевна', 'Backend-разработчик', '2026-03-01', NULL,         22.50, TRUE),
    (2, 'Смирнов',   'Дмитрий',  'Олегович',  'Frontend-разработчик','2026-03-15', NULL,         20.00, TRUE),
    (2, 'Волкова',   'Екатерина','Павловна',  'QA-инженер',          '2026-04-01', '2026-07-31', 15.00, FALSE);

INSERT INTO programmers
(project_id, last_name, first_name, middle_name, position,
 work_start_date, work_end_date, hourly_rate, is_full_time) VALUES
    (3, 'Николаев', 'Артём',   'Русланович', 'Fullstack-разработчик', '2026-08-15', NULL, 27.00, TRUE),
    (3, 'Титова',   'Анна',    'Дмитриевна', 'UI/UX дизайнер',        '2026-09-01', NULL, 19.00, FALSE);