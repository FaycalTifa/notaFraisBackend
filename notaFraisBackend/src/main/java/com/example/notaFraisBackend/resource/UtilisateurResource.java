package com.example.notaFraisBackend.resource;

import com.example.notaFraisBackend.dto.LoginRequestDTO;
import com.example.notaFraisBackend.entities.Utilisateur;
import com.example.notaFraisBackend.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Autorise Angular
@RequestMapping("/api/utilisateurs")
public class UtilisateurResource {



}
