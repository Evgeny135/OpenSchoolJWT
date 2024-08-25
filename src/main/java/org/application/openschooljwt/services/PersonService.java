package org.application.openschooljwt.services;

import org.application.openschooljwt.dto.PersonDTO;
import org.application.openschooljwt.models.Person;
import org.application.openschooljwt.models.Role;
import org.application.openschooljwt.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public String login(PersonDTO personDTO) {
        Optional<Person> person = personRepository.findPersonByEmail(personDTO.getEmail());
        if (person.isPresent()){
            if (passwordEncoder.matches(personDTO.getPassword(), person.get().getPassword())){
                return tokenService.generateToken(person.get().getEmail(), Role.USER.toString());
            }else{
                throw new RuntimeException("Неверный пароль");
            }
        }else{
            throw new RuntimeException("Пользователя не существует");
        }
    }

    public void register(PersonDTO personDTO) {
        Optional<Person> personByEmail = personRepository.findPersonByEmail(personDTO.getEmail());
        if (personByEmail.isEmpty()) {
            Person person = new Person();
            person.setEmail(personDTO.getEmail());
            person.setPassword(passwordEncoder.encode(personDTO.getPassword()));

            person.setRole(Role.USER);

            personRepository.save(person);
        } else {
            throw new RuntimeException("Пользователь зарегестрирован");
        }
    }
}
