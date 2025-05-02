CREATE TABLE if not exists player_statistics (
    id UUID PRIMARY KEY,
    season_id UUID REFERENCES season(id),
    player_id UUID REFERENCES player(id),
    scored_goals INT,
    playing_time_seconds INT,
    UNIQUE (season_id, player_id)
);
