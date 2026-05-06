package com.iewtp.auth_service.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue
    private UUID id;

    private String token;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}