package api.championship_management.model;

<<<<<<< Updated upstream
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
=======
import lombok.*;
>>>>>>> Stashed changes
@Data
public class player {
        private UUID id;
        private String name;
        private Integer number;
        private PlayerPosition position;
        private String nationality;
        private Integer age;
}
