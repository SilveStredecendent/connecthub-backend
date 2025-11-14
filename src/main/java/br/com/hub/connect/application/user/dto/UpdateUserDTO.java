package br.com.hub.connect.application.user.dto;

import br.com.hub.connect.domain.user.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") String name,

    @Email(message = "Email must be valid") String email,

    @Size(min = 6, message = "Password must be at least 6 characters") String password,

    @Size(min = 11, max = 14, message = "CPF must be between 11 and 14 characters") String CPF,

    Long enrollmentId,

    String phone,

    UserRole role) {
}
