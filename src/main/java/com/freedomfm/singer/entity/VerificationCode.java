package com.freedomfm.singer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "verificationCodes")
public class VerificationCode {
    @Id
    @Column(name = "user_id")
    private long id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Singer singer;

    private String code;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
