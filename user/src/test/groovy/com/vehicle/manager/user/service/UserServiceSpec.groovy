package com.vehicle.manager.user.service

import com.vehicle.manager.commons.enumeration.TokenType
import com.vehicle.manager.user.dao.Role
import com.vehicle.manager.user.dao.Token
import com.vehicle.manager.user.dao.User
import com.vehicle.manager.user.repository.UserRepository
import com.vehicle.manager.user.service.email.impl.EmailServiceImpl
import jakarta.persistence.EntityNotFoundException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class UserServiceSpec extends Specification {
    UserRepository userRepository = Mock(UserRepository)
    PasswordEncoder passwordEncoder = Mock(PasswordEncoder)
    RoleService roleService = Mock(RoleService)
    EmailServiceImpl emailService = Mock(EmailServiceImpl)
    TokenService tokenService = Mock(TokenService)
    UserService userService = new UserService(userRepository, passwordEncoder, emailService, tokenService, roleService)

    def "register_ValidUser_ReturnsUser"() {
        given:
        def user = Mock(User)
        def role = Mock(Role)
        def variables = new HashMap<String, Object>()
        def uuid = UUID.randomUUID()
        def link = "http://localhost:8184/api/v1/tokens/" + uuid
        def email = "test@example"
        variables.put("nickName", "TestNickName")
        variables.put("url", link)

        when:
        def result = userService.register(user)

        then:
        1 * user.getPassword() >> "password123"
        1 * passwordEncoder.encode("password123") >> "encodedPassword123"
        1 * user.setPassword("encodedPassword123")
        1 * roleService.getRoleByName("USER") >> role
        1 * user.setRoles(Collections.singleton(role))
        1 * userRepository.save(user) >> user
        1 * tokenService.save(_ as Token) >> {
            def token = it[0]
            token.id == UUID.randomUUID()
            token.expirationDate != null
            token.tokenType == TokenType.EMAIL_ACTIVATION
            token.user == user
            token.id = uuid
            return token
        }
        1 * user.getNickName() >> "TestNickName"
        1 * user.getEmail() >> email
        1 * emailService.sendMail("Account activation TEMPLATE", variables, email)
        0 * _
        result == user
    }

    def "askForAnAdminRole_ValidInput_Success"() {
        given:
        def user = Mock(User)
        def admin1 = Mock(User)
        def admin2 = Mock(User)
        def admins = [admin1, admin2]
        def adminEmails = ["admin_test1@example", "admin_test2@example"]
        def nickName = "TestNickName"
        def variables = new HashMap<>()
        def id = UUID.randomUUID()
        def url = "http://localhost:8184/api/v1/users/" + id + "/give-admin-role"
        variables.put("nickName", nickName)
        variables.put("url", url)
        def securityContext = Mock(SecurityContext)
        def authentication = Mock(Authentication)
        SecurityContextHolder.setContext(securityContext)
        def email = "test@example"

        when:
        userService.askForAnAdminRole()

        then:
        1 * userRepository.findByRolesName("ADMIN") >> admins
        1 * securityContext.getAuthentication() >> authentication
        1 * authentication.getName() >> email
        1 * userRepository.findByEmail(email) >> Optional.of(user)
        1 * user.getNickName() >> nickName
        1 * user.getId() >> id
        1 * admin1.getEmail() >> "admin_test1@example"
        1 * admin2.getEmail() >> "admin_test2@example"
        1 * emailService.sendMail("Admin role request TEMPLATE", variables, adminEmails)
        0 * _
    }

    def "askForAnAdminRole_ThrowsEntityNotFoundException"() {
        given:
        def admins = [Mock(User), Mock(User)]
        def securityContext = Mock(SecurityContext)
        def authentication = Mock(Authentication)
        SecurityContextHolder.setContext(securityContext)
        def email = "test@example"

        when:
        userService.askForAnAdminRole()

        then:
        1 * userRepository.findByRolesName("ADMIN") >> admins
        1 * securityContext.getAuthentication() >> authentication
        1 * authentication.getName() >> email
        1 * userRepository.findByEmail(email) >> Optional.empty()
        0 * _
        thrown EntityNotFoundException
    }

    def "giveAdminRoleToUser_ValidId_Success"() {
        given:
        def id = UUID.randomUUID()
        def userDb = Mock(User)
        def roles = [Mock(Role), Mock(Role)]
        def adminRole = Mock(Role)
        def variables = new HashMap<>()
        def email = "test@example"
        variables.put("nickName", "TestNickName")

        when:
        userService.giveAdminRoleToUser(id)

        then:
        1 * userRepository.getReferenceById(id) >> userDb
        1 * userDb.getRoles() >> roles
        1 * roleService.getRoleByName("ADMIN") >> adminRole
        1 * userDb.getNickName() >> "TestNickName"
        1 * userDb.getEmail() >> email
        1 * emailService.sendMail("Give admin role TEMPLATE", variables, email)
        0 * _
    }

    def "takeAwayAdminRoleFromUser_ValidId_Success"() {
        given:
        def userId = UUID.randomUUID()
        def roleId = UUID.randomUUID()
        def userDb = Mock(User)
        def roles = [Mock(Role), Mock(Role)]
        def adminRole = Mock(Role)
        def variables = new HashMap<>()
        def email = "test@example"
        variables.put("nickName", "TestNickName")
        variables.put("adminName", "michalParchas69")
        def admin = Mock(User)
        def securityContext = Mock(SecurityContext)
        def authentication = Mock(Authentication)
        SecurityContextHolder.setContext(securityContext)

        when:
        userService.takeAwayAdminRoleFromUser(userId, roleId)

        then:
        1 * userRepository.getReferenceById(userId) >> userDb
        1 * userDb.getRoles() >> roles
        1 * roleService.getReferenceById(roleId) >> adminRole
        1 * userDb.getNickName() >> "TestNickName"
        1 * securityContext.getAuthentication() >> authentication
        1 * authentication.getName() >> email
        1 * userRepository.findByEmail(email) >> Optional.of(admin)
        1 * admin.getNickName() >> "michalParchas69"
        1 * userDb.getEmail() >> email
        1 * emailService.sendMail("Remove admin role TEMPLATE", variables, email)
        0 * _
    }

    def "takeAwayAdminRoleFromUser_ThrowsEntityNotFoundException"() {
        given:
        def userId = UUID.randomUUID()
        def roleId = UUID.randomUUID()
        def userDb = Mock(User)
        def roles = [Mock(Role), Mock(Role)]
        def adminRole = Mock(Role)
        def variables = new HashMap<>()
        def email = "test@example"
        variables.put("nickName", "TestNickName")
        variables.put("adminName", "michalParchas")
        def securityContext = Mock(SecurityContext)
        def authentication = Mock(Authentication)
        SecurityContextHolder.setContext(securityContext)

        when:
        userService.takeAwayAdminRoleFromUser(userId, roleId)

        then:
        1 * userRepository.getReferenceById(userId) >> userDb
        1 * userDb.getRoles() >> roles
        1 * roleService.getReferenceById(roleId) >> adminRole
        1 * userDb.getNickName() >> "TestNickName"
        1 * securityContext.getAuthentication() >> authentication
        1 * authentication.getName() >> email
        1 * userRepository.findByEmail(email) >> Optional.empty()
        0 * _
        thrown EntityNotFoundException
    }

    def "sendEmailToResetPassword_ValidEmail_Success"() {
        given:
        def user = Mock(User)
        def variables = new HashMap<>()
        def uuid = UUID.randomUUID()
        def email = "test@example"
        def nickname = "TestNickName"
        def url = "http://localhost:8184/api/v1/users/reset-password/" + uuid
        variables.put("nickName", nickname)
        variables.put("url", url)

        when:
        userService.sendEmailToResetPassword(email)

        then:
        1 * userRepository.findByEmail("test@example") >> Optional.of(user)
        1 * tokenService.save(_ as Token) >> {
            def token = it[0]
            token.id == UUID.randomUUID()
            token.expirationDate != null
            token.tokenType == TokenType.EMAIL_ACTIVATION
            token.user == user
            token.id = uuid
            return token
        }
        1 * user.getNickName() >> nickname
        1 * emailService.sendMail("Reset password TEMPLATE", variables, email)
        0 * _
    }

    def "sendEmailToResetPassword_ThrowsEntityNotFoundException"() {
        given:
        def email = "test@example"

        when:
        userService.sendEmailToResetPassword(email)

        then:
        1 * userRepository.findByEmail("test@example") >> Optional.empty()
        0 * _
        thrown EntityNotFoundException
    }

    def "resetPassword_ValidId_Success"() {
        given:
        def newPassword = "testNewPassword"
        def encodedNewPassword = "encodedTestNewPassword"
        def id = UUID.randomUUID()
        def passwordResetToken = Mock(Token)
        def user = Mock(User)

        when:
        userService.resetPassword(id, newPassword)

        then:
        1 * tokenService.getByIdAndType(id, TokenType.PASSWORD_RESET) >> passwordResetToken
        1 * passwordResetToken.getUser() >> user
        1 * passwordEncoder.encode(newPassword) >> encodedNewPassword
        1 * user.setPassword(encodedNewPassword)
        1 * passwordResetToken.getId() >> id
        1 * tokenService.delete(id)
        0 * _
    }
}
