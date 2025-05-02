CREATE TABLE if not exists club (
    id UUID PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    acronym VARCHAR(10),
    year_creation INT,
    stadium VARCHAR(100),
    coach_name VARCHAR(100),
    coach_nationality VARCHAR(100)
);
