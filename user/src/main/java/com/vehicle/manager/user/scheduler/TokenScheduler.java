package com.vehicle.manager.user.scheduler;

import com.vehicle.manager.commons.enumeration.TokenType;
import com.vehicle.manager.user.dao.Token;
import com.vehicle.manager.user.dao.User;
import com.vehicle.manager.user.repository.TokenRepository;
import com.vehicle.manager.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenScheduler {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 */48 * * *")
    public void updateTokenStatus() {
        List<Token> expiredTokens = tokenRepository.findByExpirationDateBefore(LocalDateTime.now());
        List<Token> expiredEmailActivationTokens = new ArrayList<>();
        List<Token> expiredOtherTokens = new ArrayList<>();
        for (Token expiredToken : expiredTokens) {
            if (expiredToken.getTokenType().equals(TokenType.EMAIL_ACTIVATION)) {
                expiredEmailActivationTokens.add(expiredToken);
            } else {
                expiredOtherTokens.add(expiredToken);
            }
        }
        tokenRepository.deleteByIdIn(expiredOtherTokens.stream()
                .map(Token::getId)
                .toList());
        userRepository.deleteByIdIn(expiredEmailActivationTokens.stream()
                .map(Token::getUser)
                .map(User::getId)
                .toList());
    }
}
