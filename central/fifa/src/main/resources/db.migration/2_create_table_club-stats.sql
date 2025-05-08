
   CREATE TABLE if not exists club_stats (
       id UUID PRIMARY KEY,
       name VARCHAR(100) NOT NULL,
       acronym VARCHAR(10),
       championship_name VARCHAR(100),
       goals_scored INT DEFAULT 0,
       ranking_points INT DEFAULT 0,
       goals_conceded INT DEFAULT 0,
       goal_difference INT DEFAULT 0,
       clean_sheet_count INT DEFAULT 0,
       coach_name VARCHAR(100),
       coach_nationality VARCHAR(100),
       stadium VARCHAR(100),
       year_creation INT,
   );