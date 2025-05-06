package api.bundesliga.service;


import api.bundesliga.dao.operations.ClubCrudOperations;
import api.bundesliga.dao.operations.PlayerCrudOperations;

import api.bundesliga.endpoint.rest.PlayerRest;
import api.bundesliga.endpoint.rest.StatPlayerRest;
import api.bundesliga.entity.Club;
import api.bundesliga.entity.Player;
import api.bundesliga.entity.StatPlayer;
import api.bundesliga.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerCrudOperations playerCrudOperations;
    private final ClubCrudOperations clubCrudOperations;

    public List<Player> getAll(Integer page, Integer size) {
        return playerCrudOperations.getAll(page, size);
    }

    public List<Player> saveAll(List<Player> players) {
        return playerCrudOperations.saveAll(players);
    }

    public StatPlayer getStatPlayer(String player_id, Integer seasonYear) {
        return playerCrudOperations.findByIdPlayer(player_id, seasonYear);
    }

    public List<Player> savePlayerOnClub(String clubId, List<PlayerRest> players) {
        Club club = clubCrudOperations.findById(clubId);
        if (club == null) {
            throw new EntityNotFoundException("Club not found");
        }

        List<Player> addedPlayers = new ArrayList<>();

        for (PlayerRest playerrest : players) {
            if (playerCrudOperations.existsByClubIdAndNumber(clubId, playerrest.getNumber())) {
                throw new IllegalArgumentException("Player number " + playerrest.getNumber() + " already used in this club");
            }

            Player player = new Player();
            player.setId(UUID.randomUUID().toString());
            player.setName(playerrest.getName());
            player.setNumber(playerrest.getNumber());
            player.setNationality(playerrest.getNationality());
            player.setAge(playerrest.getAge());
            player.setPlayerPosition(playerrest.getPlayerPosition());
            player.setClub(club);


            addedPlayers.add(player);
        }

        return playerCrudOperations.saveAll(addedPlayers);

    }

    public List<StatPlayerRest> getStat() {
        return playerCrudOperations.getStat();
    }
}