package com.vehicle.manager.user.dao;


import com.vehicle.manager.commons.enumeration.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime expirationDate;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    @ManyToOne
    private User user;
}
