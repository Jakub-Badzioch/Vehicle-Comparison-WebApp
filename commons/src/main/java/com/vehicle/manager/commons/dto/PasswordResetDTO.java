package com.vehicle.manager.commons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetDTO {
    private UUID id;
    @NotBlank(message = "Incorrect password. Can't be null and length with/without whitespaces must be longer than 0.")
    @Pattern(regexp = "((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])).{10,}",
            message = "Incorrect password. Must be at least: 1 int, 1 lowercase, 1 uppercase, 10 chars.")
    private String newPassword;
}
