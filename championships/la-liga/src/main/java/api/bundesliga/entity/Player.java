package api.bundesliga.entity;

import api.bundesliga.entity.Club;
import api.bundesliga.entity.PlayerPosition;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Data
public class Player {
    private String id;
    private String name;
    private Integer number;
    private String nationality;
    private Integer age;
    private PlayerPosition playerPosition;
    private Club club;
}