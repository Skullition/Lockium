package dev.skullition.lockium.service;

import com.zaxxer.hikari.HikariDataSource;
import io.github.freya022.botcommands.api.core.db.HikariSourceSupplier;
import io.github.freya022.botcommands.api.core.service.annotations.BService;
import org.jetbrains.annotations.NotNull;

@BService
public class DatabaseSource implements HikariSourceSupplier {
    private final HikariDataSource dataSource;

    public DatabaseSource(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @NotNull
    @Override
    public HikariDataSource getSource() {
        return dataSource;
    }
}
