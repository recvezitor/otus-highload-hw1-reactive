package com.dimas.persistence;

import com.dimas.domain.entity.Person;
import com.dimas.exception.NotFoundJdbcException;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.dimas.util.Const.SCHEMA_NAME;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class PersonRepository {

    private final PgPool pgPool;

    public Uni<Person> create(Person request) {
        log.info("persisting request person={}", request);
        var entity = request.withId(isNull(request.getId()) ? UUID.randomUUID() : request.getId());
        return pgPool.withTransaction(conn -> conn.preparedQuery("""
                        INSERT INTO %s.person (id, first_name, second_name, birthdate, biography, city, created_at, password)
                                    VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
                        """.formatted(SCHEMA_NAME))
                .execute(map(entity))
                .flatMap(e -> Uni.createFrom().item(entity)));
    }


    public Uni<Person> getById(UUID id) {
        return findById(id)
                .onItem().ifNull().failWith(() -> new NotFoundJdbcException("Person not found for id='%s'".formatted(id)));
    }

    public Uni<Person> findById(UUID id) {
        final var query = """
                 select * from %s.person where id = $1 LIMIT 1
                """.formatted(SCHEMA_NAME);
        return pgPool.preparedQuery(query)
                .execute(Tuple.tuple().addUUID(id))
                .onItem().transformToUni(rowSet -> {
                    for (Row row : rowSet) {
                        return Uni.createFrom().item(map(row));//return first item
                    }
                    return Uni.createFrom().item(null);
                });
    }

    public Uni<Person> getByName(String name) {
        return findByName(name)
                .onItem().ifNull().failWith(() -> new NotFoundJdbcException("Person not found for name='%s'".formatted(name)));
    }

    public Uni<Person> findByName(String name) {
        final var query = """
                select * from %s.person where first_name = $1  LIMIT 1
                """.formatted(SCHEMA_NAME);
        return pgPool.preparedQuery(query)
                .execute(Tuple.tuple().addString(name))
                .onItem().transformToUni(rowSet -> {
                    for (Row row : rowSet) {
                        return Uni.createFrom().item(map(row));//return first item
                    }
                    return Uni.createFrom().item(null);
                });
    }

    private Person map(Row row) {
        return Person.builder()
                .id(row.getUUID("id"))
                .firstName(row.getString("first_name"))
                .secondName(row.getString("second_name"))
                .birthdate(row.getLocalDate("birthdate"))
                .biography(row.getString("biography"))
                .city(row.getString("city"))
                .createdAt(row.getLocalDateTime("created_at"))
                .updatedAt(row.getLocalDateTime("updated_at"))
                .password(row.getString("password"))
                .build();
    }

    private Tuple map(Person request) {
        return Tuple.tuple()
                .addUUID(isNull(request.getId()) ? UUID.randomUUID() : request.getId())//TODO delegate uuid generation to DB
                .addString(request.getFirstName())
                .addString(request.getSecondName())
                .addLocalDate(request.getBirthdate())
                .addString(request.getBiography())
                .addString(request.getCity())
                .addLocalDateTime(LocalDateTime.now())
                .addString(request.getPassword());
    }

}
