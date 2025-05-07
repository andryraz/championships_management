package api.bundesliga.dao.mapper;


import api.bundesliga.dao.operations.ClubCrudOperations;
import api.bundesliga.entity.Club;
import api.bundesliga.entity.Player;
import api.bundesliga.entity.PlayerPosition;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PlayerMapper implements Function<ResultSet, Player> {
    //private final ClubCrudOperations clubCrudOperations;

    @SneakyThrows
    @Override
    public Player apply(ResultSet resultSet) {
       //String id = resultSet.getString("id");

        Player player = new Player();
        player.setId(resultSet.getString("id"));
        player.setName(resultSet.getString("name"));
        player.setPlayerPosition(PlayerPosition.valueOf(resultSet.getString("position")));
        player.setNationality(resultSet.getString("nationality"));
        player.setNumber(resultSet.getInt("number"));
        player.setAge(resultSet.getInt("age"));
       // Club club = clubCrudOperations.findById(id);
        //player.setClub(club);
        return player;
    }
}

