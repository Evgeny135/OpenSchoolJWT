package org.application.openschooljwt.services;

import org.application.openschooljwt.dto.PersonDTO;
import org.application.openschooljwt.models.Person;
import org.application.openschooljwt.models.Role;
import org.application.openschooljwt.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PersonServiceTest {

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private PersonService personService;

    private PersonDTO personDTO;

    @BeforeEach
    public void setUp(){
        personDTO = new PersonDTO();
        personDTO.setEmail("test@example.com");
        personDTO.setPassword("password");
    }

    @Test
    void givenCorrectUser_whenLogin_thenLoginSuccess() {
        Person person = new Person();
        person.setEmail(personDTO.getEmail());
        person.setPassword("encodedPassword");

        when(personRepository.findPersonByEmail(personDTO.getEmail())).thenReturn(Optional.of(person));
        when(passwordEncoder.matches(personDTO.getPassword(), "encodedPassword")).thenReturn(true);
        when(tokenService.generateToken(person.getEmail(), Role.USER.toString())).thenReturn("token");

        String token = personService.login(personDTO);

        assertEquals("token", token);
    }

    @Test
    void givenUserWithInvalidPassword_whenLogin_thenLoginFailed() {
        Person person = new Person();
        person.setEmail(personDTO.getEmail());
        person.setPassword("encodedPassword");

        when(personRepository.findPersonByEmail(personDTO.getEmail())).thenReturn(Optional.of(person));
        when(passwordEncoder.matches(personDTO.getPassword(), "encodedPassword")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> personService.login(personDTO));
        assertEquals("Неверный пароль", exception.getMessage());
    }

    @Test
    void givenNotExistsUser_whenLogin_thenThrowException() {
        when(personRepository.findPersonByEmail(personDTO.getEmail())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> personService.login(personDTO));
        assertEquals("Пользователя не существует", exception.getMessage());
    }

    @Test
    void givenNewUser_whenRegister_thenSuccessRegistration() {
        when(personRepository.findPersonByEmail(personDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(personDTO.getPassword())).thenReturn("encodedPassword");

        personService.register(personDTO);

        verify(personRepository).save(argThat(person ->
                "test@example.com".equals(person.getEmail()) &&
                        "encodedPassword".equals(person.getPassword()) &&
                        Role.USER.equals(person.getRole())
        ));
    }

    @Test
    void givenExistsUser_whenRegister_thenThrowException() {
        Person existingPerson = new Person();
        existingPerson.setEmail(personDTO.getEmail());

        when(personRepository.findPersonByEmail(personDTO.getEmail())).thenReturn(Optional.of(existingPerson));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> personService.register(personDTO));
        assertEquals("Пользователь зарегестрирован", exception.getMessage());
    }
}
