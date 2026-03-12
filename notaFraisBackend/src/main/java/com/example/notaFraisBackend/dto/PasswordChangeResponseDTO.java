package com.example.notaFraisBackend.dto;

public class PasswordChangeResponseDTO {  private String message;
    private boolean success;

    public PasswordChangeResponseDTO(String message) {
        this.message = message;
        this.success = message.contains("succès");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
