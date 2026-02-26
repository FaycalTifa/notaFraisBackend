package com.example.notaFraisBackend.security;

import com.example.notaFraisBackend.entities.entity.Collaborateur;
import com.example.notaFraisBackend.repository.CollaborateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService  implements UserDetailsService {

    @Autowired
    private CollaborateurRepository collaborateurRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Collaborateur collaborateur = collaborateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));

        return new User(
                collaborateur.getEmail(),
                collaborateur.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + collaborateur.getRole().name()))
        );
    }
}

