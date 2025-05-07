CREATE TABLE if not exists goal (
    id UUID PRIMARY KEY,
    match_id UUID REFERENCES match(id),
    scorer_id UUID REFERENCES player(id),
    club_id UUID REFERENCES club(id),
    minute_of_goal INT CHECK (minute_of_goal BETWEEN 1 AND 90),
    own_goal BOOLEAN DEFAULT FALSE
);