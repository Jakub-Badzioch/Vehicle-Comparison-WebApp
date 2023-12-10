package com.vehicle.manager.user.controller;


import com.vehicle.manager.commons.dto.PasswordResetDTO;
import com.vehicle.manager.commons.dto.model.UserDTO;
import com.vehicle.manager.user.mapper.UserMapper;
import com.vehicle.manager.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO register(@RequestBody @Valid UserDTO userDTO) {
        return userMapper.toDto(userService.register(userMapper.toEntity(userDTO)));
    }

    @PreAuthorize("isAuthenticated() && !hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/ask-for-admin-role")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void askForAnAdminRole() {
        userService.askForAnAdminRole();
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{userId}/give-admin-role")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void giveAdminRoleToUser(@PathVariable UUID userId) {
        userService.giveAdminRoleToUser(userId);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{userId}/roles/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void takeAwayAdminRoleFromUser(@PathVariable UUID userId, @PathVariable UUID roleId) {
        userService.takeAwayAdminRoleFromUser(userId, roleId);
    }

    @PostMapping("/send-reset-password-email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendResetPasswordEmail(@RequestParam String email) {
        userService.sendEmailToResetPassword(email);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@RequestBody @Valid PasswordResetDTO passwordResetDTO) {
        userService.resetPassword(passwordResetDTO.getId(), passwordResetDTO.getNewPassword());
    }
}
