// Coach.java
package com.gestion.fifa.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coach {
    private String name;
    private String nationality;
}
