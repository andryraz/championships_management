package api.bundesliga.endpoint.rest;

import api.bundesliga.entity.Club;
import api.bundesliga.entity.PlayerPosition;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Data
public class PlayerRest {
    private String id;
    private String name;
    private Integer number;
    private String nationality;
    private Integer age;
    private PlayerPosition playerPosition;
}