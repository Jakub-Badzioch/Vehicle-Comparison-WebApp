package com.vehicle.manager.user.repository;

import com.vehicle.manager.commons.enumeration.TokenType;
import com.vehicle.manager.user.dao.Token;
import com.vehicle.manager.user.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {

    List<Token> findByExpirationDateBefore(LocalDateTime expirationDate);

    List<Token> findAllByUserAndTokenType(User user, TokenType passwordReset);

    void deleteByIdIn(List<UUID> list);

    Optional<Token> findByIdAndTokenType(UUID tokenId, TokenType tokenType);
}
