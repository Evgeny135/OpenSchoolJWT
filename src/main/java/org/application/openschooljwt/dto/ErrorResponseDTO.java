package org.application.openschooljwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "DTO при ошибке выполнения запроса")
public class ErrorResponseDTO {

    @Schema(description = "Время ошибки запроса")
    private Date timestamp;

    @Schema(description = "Статус запроса",example = "403")
    private int status;

    @Schema(description = "Название ошибки",example = "Forbidden")
    private String error;

    @Schema(description = "URL на который отправлен запрос")
    private String path;

    public ErrorResponseDTO() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
