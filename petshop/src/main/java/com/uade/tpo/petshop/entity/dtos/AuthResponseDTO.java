package com.uade.tpo.petshop.entity.dtos;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponseDTO {
    private String accessToken;
}
