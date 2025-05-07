package api.bundesliga.service;


import api.bundesliga.dao.operations.ClubCrudOperations;
import api.bundesliga.dao.operations.PlayerCrudOperations;
import api.bundesliga.dao.operations.TransfertCrudOperations;
import api.bundesliga.endpoint.mapper.PlayerRestMapper;
import api.bundesliga.endpoint.rest.PlayerRest;
import api.bundesliga.endpoint.rest.StatClubRest;
import api.bundesliga.entity.*;

import lombok.RequiredArgsConstructor;
import api.bundesliga.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import api.bundesliga.service.exception.BadRequestException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubCrudOperations clubCrudOperations;
    private final TransfertCrudOperations transfertCrudOperations;

    public List<Club> getAll(Integer page, Integer size) {
        return clubCrudOperations.getAll(page, size);
    }

    public List<Player> getPlayerByIdClub(String id) {
        return clubCrudOperations.findPlayerByIdClub(id);
    }

    public List<StatClub> getStatForSpecificSeason(Integer seasonYear) {
        return clubCrudOperations.getStatForSpecificSeason(seasonYear);
    }

    public List<Club> saveAll(List<Club> clubs) {
        return clubCrudOperations.saveAll(clubs);
    }

    public List<PlayerRest> replacePlayers(String clubId, List<PlayerRest> newPlayers) {

        if (!clubCrudOperations.existsClubById(clubId)) {
            throw new ResourceNotFoundException("Club not found: " + clubId);
        }

        for (PlayerRest p : newPlayers) {
            if (p.getId() != null && clubCrudOperations.existsPlayerInAnotherClub(p.getId(), clubId)) {
                throw new BadRequestException("Player already belongs to another club: " + p.getId());
            }
        }
        clubCrudOperations.detachAllPlayersFromClub(clubId);

        for (PlayerRest rest : newPlayers) {
            Player p = PlayerRestMapper.toEntity(rest);
            Club clubRef = new Club();
            clubRef.setId(clubId);
            p.setClub(clubRef);
            clubCrudOperations.saveOrUpdatePlayer(p);
        }

        List<Player> updatedPlayers = clubCrudOperations.findPlayerByIdClub(clubId);
        return updatedPlayers.stream().map(PlayerRestMapper::toDTO).toList();
//        return clubCrudOperations.findPlayerByIdClub(clubId);
    }

    public List<StatClubRest> getStat() {
        return clubCrudOperations.getStat();
    }

}
