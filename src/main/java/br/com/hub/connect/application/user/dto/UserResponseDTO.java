package br.com.hub.connect.application.user.dto;

import java.time.LocalDateTime;
import br.com.hub.connect.domain.user.enums.UserRole;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        UserRole role,
        Double xp,
        Integer level,
        String avatarUrl,
        LocalDateTime createdAt) {
}