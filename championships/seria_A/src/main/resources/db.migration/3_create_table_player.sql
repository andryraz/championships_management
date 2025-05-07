CREATE TABLE if not exists player (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    number INT NOT NULL,
    position player_position,
    nationality VARCHAR(100),
    age INT,
    club_id UUID REFERENCES club(id),
    UNIQUE (club_id, number)
);
