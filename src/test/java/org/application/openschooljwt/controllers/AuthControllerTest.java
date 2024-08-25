package org.application.openschooljwt.controllers;

import org.application.openschooljwt.dto.PersonDTO;
import org.application.openschooljwt.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
public class AuthControllerTest {
    @InjectMocks
    private AuthController authController;
    @Mock
    private PersonService personService;

    private PersonDTO personDTO;

    @BeforeEach
    public void setUp(){
        personDTO = new PersonDTO();
        personDTO.setEmail("test@example.ru");
        personDTO.setPassword("password");
    }

    @Test
    void givenNotExistsUser_whenRegister_thenSuccessRegister() {
        doNothing().when(personService).register(personDTO);

        ResponseEntity<String> response = authController.register(personDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Успешно", response.getBody());
    }

    @Test
    void givenExistsUser_whenRegister_thenFailedRegister() {
        doThrow(new RuntimeException("Пользователь уже зарегестрирован"))
                .when(personService).register(personDTO);

        ResponseEntity<String> response = authController.register(personDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Пользователь уже зарегестрирован", response.getBody());
    }

    @Test
    void givenExistsUser_whenLogin_thenReturnToken() {
        String token = "afbafbao";
        when(personService.login(personDTO)).thenReturn(token);

        ResponseEntity<String> response = authController.login(personDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody());
    }

    @Test
    void givenNotExistsUser_whenLogin_thenNotReturnToken() {
        when(personService.login(personDTO))
                .thenThrow(new RuntimeException("Пользователя не существует"));

        ResponseEntity<String> response = authController.login(personDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Пользователя не существует", response.getBody());
    }

    @Test
    void givenExistUserWithWrongPassword_whenLogin_thenReturnConflict() {
        when(personService.login(personDTO))
                .thenThrow(new RuntimeException("Неверный пароль"));

        ResponseEntity<String> response = authController.login(personDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Неверный пароль", response.getBody());
    }
}
