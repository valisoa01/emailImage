package com.example.demo.emailimage;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class EmailImageRepository {

  private final JdbcTemplate jdbcTemplate;

  public EmailImage save(EmailImage emailImage) {
    jdbcTemplate.update(
        "INSERT INTO email_image (id, nom_fichier, email, created_at) VALUES (?, ?, ?, ?)",
        emailImage.id(),
        emailImage.nomFichier(),
        emailImage.email(),
        Timestamp.from(emailImage.createdAt()));
    return emailImage;
  }

  public List<EmailImage> findAll() {
    return jdbcTemplate.query(
        "SELECT id, nom_fichier, email, created_at FROM email_image ORDER BY created_at DESC",
        (rs, rowNum) ->
            new EmailImage(
                UUID.fromString(rs.getString("id")),
                rs.getString("filename"),
                rs.getString("email"),
                rs.getTimestamp("created_at").toInstant()));
  }
}
