CREATE TABLE season if not exists (
                        id UUID PRIMARY KEY,
                        year INT NOT NULL,
                        alias VARCHAR(20),
                        status season_status DEFAULT 'NOT_STARTED'
);