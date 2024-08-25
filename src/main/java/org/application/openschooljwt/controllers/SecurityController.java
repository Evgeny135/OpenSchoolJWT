package org.application.openschooljwt.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.application.openschooljwt.dto.ErrorResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
@Tag(name = "Контроллер для теста аутентификации с помощью токена")
@SecurityRequirement(name = "bearerAuth")
public class SecurityController {

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Запрос для тестирования (доступен только для пользователя)",
    responses = {
            @ApiResponse(responseCode = "200",
            description = "Успешный запрос",
            content = @Content(mediaType = "text/plain",
            schema = @Schema(type = "String",example = "Вы пользователь"))),
            @ApiResponse(responseCode = "403",
            description = "В случае если пользователь не авторизован или токен неккоректен/истек",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<String> ok(){
        return ResponseEntity.ok("Вы пользователь");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Запрос для тестирования (доступен только для пользователя)",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Успешный запрос",
                            content = @Content(mediaType = "text/plain",
                                    schema = @Schema(type = "String",example = "Вы администратор"))),
                    @ApiResponse(responseCode = "403",
                            description = "В случае если администратор не авторизован или токен неккоректен/истек",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class)))
            })
    public ResponseEntity<String> admin(){
        return ResponseEntity.ok("Вы администратор");
    }

}
