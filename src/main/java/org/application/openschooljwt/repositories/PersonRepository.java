package org.application.openschooljwt.repositories;

import org.application.openschooljwt.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    public Optional<Person> findPersonByEmail(String email);

}
