package com.vehicle.manager.user.service;

import com.vehicle.manager.commons.enumeration.Type;
import com.vehicle.manager.user.dao.Token;
import com.vehicle.manager.user.dao.User;
import com.vehicle.manager.user.repository.UserRepository;
import com.vehicle.manager.user.security.SecurityUtils;
import com.vehicle.manager.user.service.email.EmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final RoleService roleService;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(roleService.getRoleByName("USER")));
        userRepository.save(user);

        Token token = Token.builder()
                .expirationDate(LocalDateTime.now().plusHours(48))
                .type(Type.EMAIL_ACTIVATION)
                .user(user)
                .build();
        tokenService.save(token);

        final Map<String, Object> variables = new HashMap<>();
        variables.put("nickName", user.getNickName());
        variables.put("url", "http://localhost:8184/api/v1/tokens/" + token.getId());
        emailService.sendMail("Account activation TEMPLATE", variables, user.getEmail());
        return user;
    }

    public void askForAnAdminRole() {
        List<User> admins = userRepository.findByRolesName("ADMIN");
        final String email = SecurityUtils.getUserName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        final Map<String, Object> variables = new HashMap<>();
        variables.put("nickName", user.getNickName());
        variables.put("url", "http://localhost:8184/api/v1/users/" + user.getId() + "/give-admin-role");
        emailService.sendMail("Admin role request TEMPLATE", variables,
                admins.stream()
                        .map(User::getEmail)
                        .toArray(String[]::new)
        );
    }

    @Transactional
    public void giveAdminRoleToUser(UUID id) {
        User userDb = userRepository.getReferenceById(id);
        userDb.getRoles().add(roleService.getRoleByName("ADMIN"));
        final Map<String, Object> variables = new HashMap<>();
        variables.put("nickName", userDb.getNickName());
        emailService.sendMail("Give admin role TEMPLATE", variables, userDb.getEmail());
    }

    @Transactional
    public void takeAwayAdminRoleFromUser(UUID userId, UUID roleId) {
        User userDb = userRepository.getReferenceById(userId);
        userDb.getRoles().remove(roleService.getReferenceById(roleId));

        final Map<String, Object> variables = new HashMap<>();
        variables.put("nickName", userDb.getNickName());


        User admin = userRepository.findByEmail(SecurityUtils.getUserName())
                .orElseThrow(EntityNotFoundException::new);



        variables.put("adminName", admin.getNickName());
        emailService.sendMail("Remove admin role TEMPLATE", variables, userDb.getEmail());
    }

    public void sendEmailToResetPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        Token token = Token.builder()
                .expirationDate(LocalDateTime.now().plusHours(48))
                .type(Type.PASSWORD_RESET)
                .user(user)
                .build();
        tokenService.save(token);
        final Map<String, Object> variables = new HashMap<>();
        variables.put("nickName", user.getNickName());
        variables.put("url", "http://localhost:8184/api/v1/users/reset-password/" + token.getId());
        emailService.sendMail("Reset password TEMPLATE", variables, email);
    }

    @Transactional
    public void resetPassword(UUID tokenId, String newPassword) {
        Token passwordResetToken = tokenService.getByIdAndType(tokenId, Type.PASSWORD_RESET);
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        tokenService.delete(passwordResetToken.getId());
    }
}