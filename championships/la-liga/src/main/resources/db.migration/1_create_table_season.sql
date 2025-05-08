CREATE TABLE if not exists season  (
                        id UUID PRIMARY KEY,
                        year INT NOT NULL,
                        alias VARCHAR(20),
                        status season_status DEFAULT 'NOT_STARTED'
);