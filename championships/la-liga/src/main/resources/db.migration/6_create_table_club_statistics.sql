CREATE TABLE if not exists club_statistics (
    id UUID PRIMARY KEY,
    season_id UUID REFERENCES season(id),
    club_id UUID REFERENCES club(id),
    ranking_points INT,
    scored_goals INT,
    conceded_goals INT,
    difference_goals INT,
    clean_sheet_number INT,
    UNIQUE (season_id, club_id)
);