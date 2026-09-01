package com.sims.backend.dto.response;

import com.sims.backend.model.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long userId;

    private String username;

    private String email;

    private Role role;

    private Boolean enabled;
}
