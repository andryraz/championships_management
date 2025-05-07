package api.bundesliga.endpoint.mapper;

import api.bundesliga.endpoint.rest.PlayerRest;
import api.bundesliga.entity.Club;
import api.bundesliga.entity.Player;
import api.bundesliga.entity.PlayerPosition;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import api.bundesliga.entity.Club;
import api.bundesliga.entity.Player;
import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PlayerRestMapper{


        public static Player toEntity(PlayerRest dto) {
            Player p = new Player();
            p.setId(dto.getId());
            p.setName(dto.getName());
            p.setNumber(dto.getNumber());
            p.setAge(dto.getAge());
            p.setNationality(dto.getNationality());
            p.setPlayerPosition(dto.getPlayerPosition());
            return p;
        }

        public static PlayerRest toDTO(Player p) {
            PlayerRest dto = new PlayerRest();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setNumber(p.getNumber());
            dto.setAge(p.getAge());
            dto.setNationality(p.getNationality());
            dto.setPlayerPosition(p.getPlayerPosition());
            return dto;
        }
    }


