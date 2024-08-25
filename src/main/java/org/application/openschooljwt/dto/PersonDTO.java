package org.application.openschooljwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO для регистрации и аутентификации")
public class PersonDTO {

    @Schema(description = "Почта пользователя",example = "test@domain.ru")
    private String email;

    @Schema(description = "Пароль пользователя",example = "testPassword")
    private String password;

    public PersonDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
