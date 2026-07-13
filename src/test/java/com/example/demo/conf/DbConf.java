package com.example.demo.conf;

import com.example.demo.PojaGenerated;
import java.util.UUID;
import org.springframework.test.context.DynamicPropertyRegistry;

/**
 * Base H2 en mémoire (mode compatibilité PostgreSQL), utilisée uniquement pour les tests. Aucune
 * dépendance à Docker n'est nécessaire.
 */
@PojaGenerated
public class DbConf {

  void configureProperties(DynamicPropertyRegistry registry) {
    // Nom de base unique par run pour éviter les collisions entre tests
    String dbName = "testdb-" + UUID.randomUUID();

    registry.add(
        "spring.datasource.url",
        () -> "jdbc:h2:mem:" + dbName + ";MODE=PostgreSQL;DB_CLOSE_DELAY=-1");
    registry.add("spring.datasource.username", () -> "sa");
    registry.add("spring.datasource.password", () -> "");
    registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
  }
}
