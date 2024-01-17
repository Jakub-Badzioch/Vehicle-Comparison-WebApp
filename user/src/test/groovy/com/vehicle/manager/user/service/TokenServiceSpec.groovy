package com.vehicle.manager.user.service

import com.vehicle.manager.commons.enumeration.Type
import com.vehicle.manager.commons.enumeration.Type
import com.vehicle.manager.user.dao.Token
import com.vehicle.manager.user.dao.User
import com.vehicle.manager.user.repository.TokenRepository
import spock.lang.Specification

class TokenServiceSpec extends Specification {
    TokenRepository tokenRepository = Mock(TokenRepository)
    TokenService tokenService = new TokenService(tokenRepository)

    def removeToken_ValidId_Success() {
        given:
        def id = UUID.randomUUID()

        when:
        tokenService.delete(id)

        then:
        1 * tokenRepository.deleteById(id)
        0 * _
    }

    def save_ValidToken_Success() {
        given:
        def token = Mock(Token)

        when:
        tokenService.save(token)

        then:
        1 * tokenRepository.save(token)
        0 * _
    }

    def "hasUserPasswordResetToken_ValidUser_ReturnsTrue"() {
        given:
        def user = Mock(User)
        def passwordResetToken = Mock(Token)
        def tokens = [passwordResetToken]
        def passwordReset = Type.PASSWORD_RESET

        when:
        def result = tokenService.hasUserPasswordResetToken(user)

        then:
        1 * tokenRepository.findAllByUserAndType(user, passwordReset) >> tokens
        0 * _
        result
    }

    def getById_ValidId_Success() {
        given:
        def id = UUID.randomUUID()
        def token = Mock(Token)

        when:
        def result = tokenService.getById(id)

        then:
        1 * tokenRepository.getReferenceById(id) >> token
        0 * _
        result == token
    }
}
