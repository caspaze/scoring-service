package ru.vsu.scoringservice.config;

import org.testcontainers.containers.PostgreSQLContainer;

public final class FixedPortPostgreSqlContainer extends PostgreSQLContainer<FixedPortPostgreSqlContainer> {

    public FixedPortPostgreSqlContainer(String dockerImageName, int hostPort) {
        super(dockerImageName);
        super.addFixedExposedPort(hostPort, POSTGRESQL_PORT);
    }
    //cтруктуры данных достаточно универсально и позхволяют при соотв. настройке использовать реализованный алгоритм с различным набором атрибутов, содержащиххся в других датасетах. Для этого необходимо
}
