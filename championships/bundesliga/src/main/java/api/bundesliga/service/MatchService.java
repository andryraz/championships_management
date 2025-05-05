package api.bundesliga.service;

import api.bundesliga.dao.operations.ClubCrudOperations;
import api.bundesliga.dao.operations.MatchCrudOperations;
import api.bundesliga.dao.operations.PlayerCrudOperations;
import api.bundesliga.dao.operations.SeasonCrudOperations;
import api.bundesliga.endpoint.rest.GoalInput;
import api.bundesliga.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchCrudOperations matchCrudOperations;
    private final PlayerCrudOperations playerCrudOperations;
    private final ClubCrudOperations clubCrudOperations;
    private final SeasonCrudOperations seasonCrudOperations;

    public List<Match> generateMatchsForSeason(Integer seasonYear) {
        Season season = seasonCrudOperations.findByYear(seasonYear);
        if (season == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Season not found");
        }

        if (season.getStatus() != SeasonStatus.STARTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The season has not yet started or has already finished");
        }

        boolean alreadyExists = matchCrudOperations.existsBySeasonId(season.getId());
        if (alreadyExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Matches are already generated for this season");
        }

        List<Club> allClubs = clubCrudOperations.getAll(1, 10);

        List<Match> matches = new ArrayList<>();

        Scorer defaultScorer = Scorer.builder()
                .player(PlayerMin.builder()
                        .id(UUID.randomUUID().toString())
                        .name("Player")
                        .number(0)
                        .build())
                .minuteOfGoal(0)
                .ownGoal(true)
                .build();

        for (int i = 0; i < allClubs.size(); i++) {
            for (int j = i + 1; j < allClubs.size(); j++) {
                Club home = allClubs.get(i);
                Club away = allClubs.get(j);

                Match match = Match.builder()
                        .id(UUID.randomUUID().toString())
                        .clubPlayingHome(MatchClub.builder()
                                .id(home.getId())
                                .name(home.getName())
                                .acronym(home.getAcronym())
                                .score(0)
                                .scorers(List.of(defaultScorer))
                                .build())
                        .clubPlayingAway(MatchClub.builder()
                                .id(away.getId())
                                .name(away.getName())
                                .acronym(away.getAcronym())
                                .score(0)
                                .scorers(List.of(defaultScorer))
                                .build())
                        .stadium(home.getStadium())
                        .matchDatetime(LocalDate.now().plusDays(matches.size()))
                        .actualStatus(MatchStatus.NOT_STARTED)
                        .seasonYear(seasonYear)
                        .build();
                matches.add(match);

                Match matchRetour = Match.builder()
                        .id(UUID.randomUUID().toString())
                        .clubPlayingHome(MatchClub.builder()
                                .id(away.getId())
                                .name(away.getName())
                                .acronym(home.getAcronym())
                                .score(0)
                                .scorers(List.of(defaultScorer))
                                .build())
                        .clubPlayingAway(MatchClub.builder()
                                .id(home.getId())
                                .name(home.getName())
                                .acronym(away.getAcronym())
                                .score(0)
                                .scorers(List.of(defaultScorer))
                                .build())
                        .stadium(away.getStadium())
                        .matchDatetime(LocalDate.now().plusDays(matches.size()))
                        .actualStatus(MatchStatus.NOT_STARTED)
                        .seasonYear(seasonYear)
                        .build();
                matches.add(matchRetour);
            }
        }

        return matchCrudOperations.saveAll(matches);
    }

    public Match addGoals(UUID matchId, List<GoalInput> goalInputs) {
        MatchStatus status = matchCrudOperations.getMatchStatus(matchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found"));

        if (status != MatchStatus.STARTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Match must be STARTED to add goals.");
        }

        for (GoalInput goal : goalInputs) {
            PlayerMin player = playerCrudOperations.findPlayerByIdentifier(goal.getScorerIdentifier())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));

            Scorer scorer = new Scorer(player, goal.getMinuteOfGoal(), false);
            matchCrudOperations.insertGoal(matchId, goal.getClubId(), scorer);
            playerCrudOperations.incrementPlayerGoals(player.getId());
        }

//        return matchCrudOperations.getMatchByIdWithDetails(matchId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Match could not be fetched"));
        return null;
    }


    public List<Match> findBySeason(int seasonYear,
                                    MatchStatus status,
                                    String clubName,
                                    LocalDate after,
                                    LocalDate beforeOrEquals) {
        return matchCrudOperations.findBySeasonWithFilters(seasonYear, status, clubName, after, beforeOrEquals);
    }
}
