package com.sims.backend.dtos.login;

import com.sims.backend.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private String accessToken;

    private String tokenType;

    private Long userId;

    private String username;

    private String email;

    private Role role;
}
