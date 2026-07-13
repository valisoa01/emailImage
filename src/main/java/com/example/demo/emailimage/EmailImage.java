package com.example.demo.emailimage;

import java.time.Instant;
import java.util.UUID;

public record EmailImage(UUID id, String nomFichier, String email, Instant createdAt) {}
