CREATE TABLE IF NOT EXISTS email_image (
    id UUID PRIMARY KEY,
    nom_fichier VARCHAR(255) NOT NULL,
    email VARCHAR(320) NOT NULL,
    created_at TIMESTAMP NOT NULL
    );
