package api.championship_management.dao.mapper;

import api.championship_management.model.player;
import api.championship_management.dao.operations.PlayerCrudOperations;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PlayerMapper implements Function<ResultSet, player> {
    private final PlayerCrudOperations playerCrudOperations;

    @SneakyThrows
    @Override
    public player apply(ResultSet resultSet) {

        player player = new player();
        player.setId(UUID.fromString(resultSet.getString("id")));
        player.setName(resultSet.getString("name"));
        player.setNumber(resultSet.getInt("number"));

        return player;
    }
}
