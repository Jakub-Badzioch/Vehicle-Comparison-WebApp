package com.vehicle.manager.user.dao;


import com.vehicle.manager.commons.enumeration.Type;
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
    private Type type;
    @ManyToOne
    private User user;
    // usable only for jwt. Needed for 'log out' functionality
    @Lob
    private String value;
}
