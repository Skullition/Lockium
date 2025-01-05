package dev.skullition.lockium.service;

import com.zaxxer.hikari.HikariDataSource;
import io.github.freya022.botcommands.api.core.db.HikariSourceSupplier;
import io.github.freya022.botcommands.api.core.service.annotations.BService;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Provides an instance of {@link HikariDataSource} and runs up Flyway migration. */
@BService
public class DatabaseSource implements HikariSourceSupplier {
  private final HikariDataSource dataSource;
  private static final Logger logger = LoggerFactory.getLogger(DatabaseSource.class);

  /** Provides an instance of {@link HikariDataSource} and runs up Flyway migration. */
  public DatabaseSource(@NotNull HikariDataSource dataSource) {
    this.dataSource = dataSource;
    createFlyway(dataSource, "bc", "bc_database_scripts");
    createFlyway(dataSource, "lockium", "db/migration/lockium");
  }

  private static void createFlyway(
      @NotNull HikariDataSource source, @NotNull String schema, @NotNull String scriptsLocation) {
    Flyway.configure()
        .dataSource(source)
        .schemas(schema)
        .locations(scriptsLocation)
        .validateMigrationNaming(true)
        .loggers("slf4j")
        .load();
    logger.info("Configured flyway for {} in {}", schema, scriptsLocation);
  }

  @NotNull
  @Override
  public HikariDataSource getSource() {
    return dataSource;
  }
}
