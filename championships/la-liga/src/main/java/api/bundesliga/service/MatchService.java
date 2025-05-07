package api.bundesliga.service;

import api.bundesliga.dao.operations.*;
import api.bundesliga.endpoint.rest.GoalInput;
import api.bundesliga.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
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
    private final GoalCrudOperations goalCrudOperations;
    private final ClubStatisticsCrudOperations clubStatisticsCrudOperations;

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

        return matchCrudOperations.getMatchByIdWithDetails(matchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Match could not be fetched"));
//        return null;
    }


    public List<Match> findBySeason(int seasonYear,
                                    MatchStatus status,
                                    String clubName,
                                    LocalDate after,
                                    LocalDate beforeOrEquals) {
        return matchCrudOperations.findBySeasonWithFilters(seasonYear, status, clubName, after, beforeOrEquals);
    }


    public void updateMatchStatus(UUID matchId, MatchStatus newStatus) throws SQLException {
        MatchEntity match = matchCrudOperations.findById(matchId);
        if (match == null) throw new IllegalArgumentException("Match not found");

        MatchStatus currentStatus = match.getStatus();
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new IllegalArgumentException("Invalid status transition");
        }

        matchCrudOperations.updateStatus(matchId, newStatus);

        if (newStatus == MatchStatus.FINISHED) {
            List<Goal> goals = goalCrudOperations.findByMatchId(matchId);
            updateStatistics(match, goals);
        }
    }

  private boolean isValidStatusTransition(MatchStatus current, MatchStatus next) {
        return (current == MatchStatus.NOT_STARTED && next == MatchStatus.STARTED)
                || (current == MatchStatus.STARTED && next == MatchStatus.FINISHED);
    }


    private void updateStatistics(MatchEntity match, List<Goal> goals) throws SQLException {
        UUID homeId = match.getClubHomeId();
        UUID awayId = match.getClubAwayId();
        UUID seasonId = match.getSeasonId();

        int homeGoals = 0;
        int awayGoals = 0;

        for (Goal g : goals) {
            boolean isHomeClub = g.getClubId().equals(homeId);
            if (g.isOwnGoal()) {
                if (isHomeClub) awayGoals++;
                else homeGoals++;
            } else {
                if (isHomeClub) homeGoals++;
                else awayGoals++;
            }
        }

        ClubStatistics homeStats = clubStatisticsCrudOperations.findBySeasonAndClub(seasonId, homeId);
        ClubStatistics awayStats = clubStatisticsCrudOperations.findBySeasonAndClub(seasonId, awayId);

        homeStats.setScoredGoals(homeStats.getScoredGoals() + homeGoals);
        homeStats.setConcededGoals(homeStats.getConcededGoals() + awayGoals);
        homeStats.setDifferenceGoals(homeStats.getScoredGoals() - homeStats.getConcededGoals());
        if (awayGoals == 0) homeStats.setCleanSheetNumber(homeStats.getCleanSheetNumber() + 1);

        awayStats.setScoredGoals(awayStats.getScoredGoals() + awayGoals);
        awayStats.setConcededGoals(awayStats.getConcededGoals() + homeGoals);
        awayStats.setDifferenceGoals(awayStats.getScoredGoals() - awayStats.getConcededGoals());
        if (homeGoals == 0) awayStats.setCleanSheetNumber(awayStats.getCleanSheetNumber() + 1);

        int homePoints = computePoints(homeGoals, awayGoals);
        int awayPoints = computePoints(awayGoals, homeGoals);
        homeStats.setRankingPoints(homeStats.getRankingPoints() + homePoints);
        awayStats.setRankingPoints(awayStats.getRankingPoints() + awayPoints);

        clubStatisticsCrudOperations.save(homeStats);
        clubStatisticsCrudOperations.save(awayStats);
    }

    private int computePoints(int goalsFor, int goalsAgainst) {
        if (goalsFor > goalsAgainst) return 3;
        if (goalsFor == goalsAgainst) return 1;
        return 0;
    }
}
