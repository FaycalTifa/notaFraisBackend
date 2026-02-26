package com.example.notaFraisBackend.dto;

import com.example.notaFraisBackend.entities.enume.Role;

public class AuthDTO {


    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginResponse {
        private String token;
        private String type = "Bearer";
        private Long id;
        private String email;
        private String nom;
        private String prenoms;
        private String role;
        private String service;

        public LoginResponse() {
            this.token = token;
            this.id = id;
            this.email = email;
            this.nom = nom;
            this.prenoms = prenoms;
            this.role = role;
            this.service = service;
        }

        public LoginResponse(String jwt, Long id, String email, String nomComplet, Role role) {
        }

        // Getters
        public String getToken() { return token; }
        public String getType() { return type; }
        public Long getId() { return id; }
        public String getEmail() { return email; }
        public String getNom() { return nom; }
        public String getPrenoms() { return prenoms; }
        public String getRole() { return role; }
        public String getService() { return service; }

        public void setDirection(String nom) {
        }

        public void setService(String nom) {
        }

        public void setSection(String nom) {
        }

        public void setType(String bearer) {
        }

        public void setToken(String jwt) {
        }

        public void setId(Long id) {
        }

        public void setEmail(String email) {
        }

        public void setNomComplet(String nomComplet) {
        }

        public void setRole(String string) {
        }
    }

    public static class ChangePasswordRequest {
        private String currentPassword;
        private String newPassword;

        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }

        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
