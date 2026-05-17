package com.conduit.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String hashPassword;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "bio")
    public String bio;
    @Column(name = "image_url")
    public String imageUrl;


}
