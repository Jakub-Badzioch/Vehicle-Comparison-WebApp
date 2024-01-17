package com.vehicle.manager.user.service;

import com.vehicle.manager.commons.enumeration.Type;
import com.vehicle.manager.user.dao.Token;
import com.vehicle.manager.user.dao.User;
import com.vehicle.manager.user.repository.TokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(UUID id) {
        tokenRepository.deleteById(id);
    }

    public void save(Token token) {
        tokenRepository.save(token);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean hasUserPasswordResetToken(User user) {
        return !tokenRepository.findAllByUserAndType(user, Type.PASSWORD_RESET)
                .isEmpty();
    }

    public Token getById(UUID id) {
        return tokenRepository.getReferenceById(id);
    }

    public Token getByIdAndType(UUID tokenId, Type type) {
        return tokenRepository.findByIdAndType(tokenId, type).orElseThrow(EntityNotFoundException::new);
    }
}
