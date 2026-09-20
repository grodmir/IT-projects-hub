-- Таблица projects
CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    customer VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,

    CONSTRAINT chk_start_end_date
                      CHECK ( start_date < end_date )
);

-- Таблица программистов
CREATE TABLE programmers (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,

    last_name VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100) NOT NULL,

    position VARCHAR(100) NOT NULL,

    work_start_date DATE NOT NULL,
    work_end_date DATE,

    hourly_rate NUMERIC(10, 2) NOT NULL,
    is_full_time BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_programmer_project
                         FOREIGN KEY (project_id)
                         REFERENCES projects(id)
                         ON DELETE CASCADE,

    CONSTRAINT chk_hourly_rate
                         CHECK ( hourly_rate > 0 ),

    CONSTRAINT chk_work_dates
                         CHECK ( work_end_date IS NULL
                             OR work_start_date < work_end_date)
);

-- Таблица users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,

    full_name VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,

    role VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_user_role
                   CHECK ( role IN ('USER', 'ADMIN', 'MANAGER') )
);

CREATE INDEX idx_programmer_project_id ON programmers(project_id);