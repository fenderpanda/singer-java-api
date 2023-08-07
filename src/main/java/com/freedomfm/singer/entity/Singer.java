package com.freedomfm.singer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "singers")
public class Singer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String email;
    private String name;
    private String phone;

    @OneToOne(mappedBy = "singer")
    @PrimaryKeyJoinColumn
    private VerificationCode verificationCode;

    @OneToMany(
            mappedBy = "singer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Song> songs = new ArrayList<>();

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public void addSong(Song song) {
        songs.add(song);
        song.setSinger(this);
    }
}
