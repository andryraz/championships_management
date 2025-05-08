CREATE TABLE if not exist player_stats (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    position VARCHAR(50),
    age INT,
     nationality VARCHAR(50),
     number INT,
    scored_goals INT DEFAULT 0,
    total_playing_time_seconds BIGINT DEFAULT 0,
    championship_name VARCHAR(100)
);