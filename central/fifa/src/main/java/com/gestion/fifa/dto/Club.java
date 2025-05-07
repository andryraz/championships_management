// Club.java
package com.gestion.fifa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Club {
    private String id;
    private String name;
    private String acronym;
    private int yearCreation; // à adapter si non disponible
    private String stadium; // idem
    private Coach coach;
}
