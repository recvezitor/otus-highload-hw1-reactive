package com.dimas.service;

import com.dimas.domain.PersonCreate;
import com.dimas.domain.entity.Person;
import com.dimas.domain.mapper.PersonMapper;
import com.dimas.persistence.PersonRepository;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static com.dimas.util.SecurityUtil.encrypt;
import static com.dimas.util.SecurityUtil.validPassword;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public Uni<Person> findById(UUID id) {
        return personRepository.getById(id);
    }

    public Uni<String> login(UUID id, String password) {
        return personRepository.getById(id)
                .map(Unchecked.function(person -> {
                    log.info("Person is found={}", person);
                    if (!validPassword(person.getPassword(), password)) {
                        throw new ForbiddenException("Неверный логин или пароль");
                    }
                    return person.getId().toString();//todo return token, replace to auth service
                }));//later make the same answer for both not found and login failed

    }

    public Uni<Person> findByName(String name) {
        return personRepository.getByName(name);
    }

    public Uni<Person> create(PersonCreate request) {
        var person = personMapper.map(request)
                .withPassword(encrypt(request.getPassword()));
        return personRepository.create(person);
    }

}
