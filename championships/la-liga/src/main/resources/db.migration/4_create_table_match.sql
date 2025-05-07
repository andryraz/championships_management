CREATE TABLE if not exists match (
    id UUID PRIMARY KEY,
    club_home_id UUID REFERENCES club(id),
    club_away_id UUID REFERENCES club(id),
    stadium VARCHAR(100),
    match_datetime TIMESTAMP,
    status match_status DEFAULT 'NOT_STARTED',
    season_id UUID REFERENCES season(id)
);
