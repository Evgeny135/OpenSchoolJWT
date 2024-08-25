package org.application.openschooljwt.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.application.openschooljwt.dto.PersonDTO;
import org.application.openschooljwt.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Контроллер для авторизации",description = "Контроллер служит для регистрации пользователя и получения токена")
public class AuthController {

    private final PersonService personService;

    @Autowired
    public AuthController(PersonService personService) {
        this.personService = personService;
    }


    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя",responses = {
            @ApiResponse(responseCode = "200",
            description = "Успешная регистрация",
            content = @Content(mediaType = "text/plain",
            schema = @Schema(type = "String", example = "Успешно"))),
            @ApiResponse(
                    responseCode = "409",
                    description = "Пользователь уже зарегистрирован",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "Пользователь уже зарегистрирован")
                    )
            )
    })
    public ResponseEntity<String> register(@RequestBody @Parameter(description = "Передача логина и пароля")
                                               PersonDTO personDTO){
        try {
            personService.register(personDTO);
            return ResponseEntity.ok("Успешно");
        }catch (Exception e){
            return new ResponseEntity<>("Пользователь уже зарегестрирован",HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Получение JWT токена", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Успешное получение токенны",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(type = "String", example = "header.payload.signature"))),
            @ApiResponse(responseCode = "409",
            description = "Неправильный логин/пароль",
            content = @Content(mediaType = "text/plain",
            schema = @Schema(type = "String",example = "Пользователя не существует")))
    })
    public ResponseEntity<String> login(@RequestBody @Parameter(description = "Передача логина и пароля") PersonDTO personDTO){
        try {
            String login = personService.login(personDTO);
            return ResponseEntity.ok(login);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }
}
