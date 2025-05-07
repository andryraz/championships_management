package api.bundesliga.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transfert {
    private String id;
    private String playerId;
    private String clubId;
    private MouvementType movementType;
    private LocalDateTime transferDate;
}
