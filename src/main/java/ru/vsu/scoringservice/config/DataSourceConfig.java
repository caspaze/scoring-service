package ru.vsu.scoringservice.config;

import com.zaxxer.hikari.HikariConfig;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class DataSourceConfig {

    @Value("${dataSource.schemaName}")
    private String schemaName;
    @Value("${dev.docker.postgres.image}")
    private String image;
    @Value("${dev.docker.postgres.port}")
    private Integer port;

    @Value("${dev.flyway.locations}")
    private String flywayLocation;
    @Value("${dev.flyway.table}")
    private String flywayTable;
    @Value("${dev.flyway.sql-migration-prefix}")
    private String flywayPrefix;

    /**
     * Docker контейнер с Postgres для локального запуска.
     */
    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(value = "dev.disable-container-db", havingValue = "false")
    public FixedPortPostgreSqlContainer postgresContainer() {
        final FixedPortPostgreSqlContainer container = new FixedPortPostgreSqlContainer(image, port)
                .withDatabaseName("ss")
                .withUsername("postgres")
                .withPassword("postgres");

        container.start();
        container.followOutput(new Slf4jLogConsumer(log));

        log.debug("Started postgres docker container with properties:");
        log.debug("url: {}, driver: {}, superuser: {}, password: {}\n", container.getJdbcUrl(),
                container.getDriverClassName(), container.getUsername(), container.getPassword());

        return container;
    }

    @Bean(destroyMethod = "close", name = "ContainerDataSource")
    @ConditionalOnProperty(value = "dev.disable-container-db", havingValue = "false")
    public DataSource containerDataSource(FixedPortPostgreSqlContainer container) {
        final DataSource dataSource = createDataSource(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword(),
                schemaName
        );

        applyFlywayMigration(dataSource);

        return dataSource;
    }

    private void applyFlywayMigration(DataSource dataSource) {
        log.info("Applying flyway migration");

        final org.flywaydb.core.api.configuration.Configuration flywayConfig = new FluentConfiguration()
                .dataSource(dataSource)
                .locations(flywayLocation)
                .table(flywayTable)
                .sqlMigrationPrefix(flywayPrefix);

        final Flyway flyway = new Flyway(flywayConfig);
        flyway.migrate();

        log.info("Flyway migration finished successfully");
    }

    private DataSource createDataSource(String url, String user, String password, String schemaName) {
        final PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setUrl(url);
        pgSimpleDataSource.setUser(user);
        pgSimpleDataSource.setPassword(password);
        pgSimpleDataSource.setApplicationName("scoring-service");
        pgSimpleDataSource.setCurrentSchema(schemaName);
        pgSimpleDataSource.setReWriteBatchedInserts(true);

        final HikariConfig config = new HikariConfig();
        config.setDataSource(pgSimpleDataSource);

        config.setMaximumPoolSize(10);
        config.setPoolName("SSLoaderDataSource");

        return new TunneledHikariDataSource(config);
    }
}
