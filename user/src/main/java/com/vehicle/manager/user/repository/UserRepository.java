package com.vehicle.manager.user.repository;


import com.vehicle.manager.user.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByRolesName(String name);

    Optional<User> findByEmail(String email);

    void deleteByIdIn(List<UUID> list);

    @Query("""
            select u from User u
            left join Token t
            on u.id = t.user.id
            where u.email = ?1
            and (t is null or t.tokenType != 'EMAIL_ACTIVATION')
            """)
    Optional<User> findByEmailAndTokensTokenTypeNot(String email);
}