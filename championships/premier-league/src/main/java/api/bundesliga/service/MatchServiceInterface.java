package api.bundesliga.service;

import api.bundesliga.entity.Match;
import api.bundesliga.entity.MatchStatus;

import java.time.LocalDate;
import java.util.List;

public interface MatchServiceInterface {
    List<Match> findMatches(int seasonYear,
                            MatchStatus status,
                            String clubName,
                            LocalDate after,
                            LocalDate beforeOrEquals);
}
