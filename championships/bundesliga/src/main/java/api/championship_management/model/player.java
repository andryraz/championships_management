package api.championship_management.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class player {
        private UUID id;
        private String name;
        private Integer number;
        private PlayerPosition position;
        private String nationality;
        private Integer age;
}
