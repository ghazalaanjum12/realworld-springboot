package com.conduit.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "follows",
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "followed_id"}))
@NoArgsConstructor
@RequiredArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    @NonNull
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id")
    @NonNull
    private User followed;
}
