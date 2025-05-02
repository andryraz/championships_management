package api.bundesliga.service;


import api.bundesliga.dao.operations.ClubCrudOperations;
import api.bundesliga.dao.operations.PlayerCrudOperations;
import api.bundesliga.endpoint.mapper.PlayerRestMapper;
import api.bundesliga.endpoint.rest.PlayerRest;
import api.bundesliga.entity.Club;
import api.bundesliga.entity.Player;

import api.bundesliga.entity.StatClub;
import api.bundesliga.entity.StatPlayer;
import lombok.RequiredArgsConstructor;
import api.bundesliga.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import api.bundesliga.service.exception.BadRequestException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubCrudOperations clubCrudOperations;

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
        // 1. Vérifier que le club existe
        if (!clubCrudOperations.existsClubById(clubId)) {
            throw new ResourceNotFoundException("Club not found: " + clubId);
        }

        // 2. Vérifier que les joueurs ne sont pas rattachés à un autre club
        for (PlayerRest p : newPlayers) {
            if (p.getId() != null && clubCrudOperations.existsPlayerInAnotherClub(p.getId(), clubId)) {
                throw new BadRequestException("Player already belongs to another club: " + p.getId());
            }
        }

        // 3. Détacher tous les joueurs actuels du club
        clubCrudOperations.detachAllPlayersFromClub(clubId);

        // 4. Sauvegarder les nouveaux joueurs avec rattachement au club
        for (PlayerRest rest : newPlayers) {
            Player p = PlayerRestMapper.toEntity(rest);
            Club clubRef = new Club();
            clubRef.setId(clubId);
            p.setClub(clubRef); // au lieu de setClubId(...)
            clubCrudOperations.saveOrUpdatePlayer(p);
        }

        List<Player> updatedPlayers = clubCrudOperations.findPlayerByIdClub(clubId);
        return updatedPlayers.stream().map(PlayerRestMapper::toDTO).toList();
//        return clubCrudOperations.findPlayerByIdClub(clubId);
    }

//    public List<PlayerRest> replacePlayers(String clubId, List<PlayerRest> players);
//    public List<Player> saveAll(List<Player> players) {
//        return playerCrudOperations.saveAll(players);
//    }


}
