package com.example.notaFraisBackend.dto;

import com.example.notaFraisBackend.entities.entity.Collaborateur;
import com.example.notaFraisBackend.entities.enume.Role;
import com.example.notaFraisBackend.entities.poste.Direction;
import com.example.notaFraisBackend.entities.poste.Section;
import com.example.notaFraisBackend.entities.poste.ServiceEntite;
import com.example.notaFraisBackend.repository.CollaborateurRepository;
import com.example.notaFraisBackend.repository.poste.DirectionRepository;
import com.example.notaFraisBackend.repository.poste.SectionRepository;
import com.example.notaFraisBackend.repository.poste.ServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component

public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CollaborateurRepository collaborateurRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("=== DÉBUT DE L'INITIALISATION DES DONNÉES ===");

        // 1. Créer les directions
        createDirections();

        // 2. Créer les services
        createServices();

        // 3. Créer les sections
        createSections();

        // 4. Créer les utilisateurs
        createUsers();

        System.out.println("=== FIN DE L'INITIALISATION DES DONNÉES ===");
    }

    private void createDirections() {
        if (directionRepository.count() == 0) {
            System.out.println("Création des directions...");

            Direction dir1 = new Direction("DIR001", "Direction Générale");
            dir1.setDescription("Direction générale de l'entreprise");
            directionRepository.save(dir1);

            Direction dir2 = new Direction("DIR002", "Direction Technique");
            dir2.setDescription("Direction en charge des aspects techniques");
            directionRepository.save(dir2);

            Direction dir3 = new Direction("DIR003", "Direction Commerciale");
            dir3.setDescription("Direction en charge des ventes et du marketing");
            directionRepository.save(dir3);

            Direction dir4 = new Direction("DIR004", "Direction des Ressources Humaines");
            dir4.setDescription("Direction en charge des ressources humaines");
            directionRepository.save(dir4);

            Direction dir5 = new Direction("DIR005", "Direction Financière");
            dir5.setDescription("Direction en charge des finances");
            directionRepository.save(dir5);

            System.out.println("✓ Directions créées avec succès");
        } else {
            System.out.println("⚠ Les directions existent déjà");
        }
    }

    private void createServices() {
        if (serviceRepository.count() == 0) {
            System.out.println("Création des services...");

            Direction dirTech = directionRepository.findByCode("DIR002").orElseThrow();
            Direction dirComm = directionRepository.findByCode("DIR003").orElseThrow();
            Direction dirRH = directionRepository.findByCode("DIR004").orElseThrow();

            // Services de la direction technique
            ServiceEntite srv1 = new ServiceEntite("SVC001", "Service Informatique", dirTech);
            srv1.setDescription("Service en charge des systèmes d'information");
            serviceRepository.save(srv1);

            ServiceEntite srv2 = new ServiceEntite("SVC002", "Service Réseaux", dirTech);
            srv2.setDescription("Service en charge des réseaux et télécommunications");
            serviceRepository.save(srv2);

            // Services de la direction commerciale
            ServiceEntite srv3 = new ServiceEntite("SVC003", "Service Commercial", dirComm);
            srv3.setDescription("Service en charge des ventes");
            serviceRepository.save(srv3);

            ServiceEntite srv4 = new ServiceEntite("SVC004", "Service Marketing", dirComm);
            srv4.setDescription("Service en charge du marketing et de la communication");
            serviceRepository.save(srv4);

            // Services de la direction RH
            ServiceEntite srv5 = new ServiceEntite("SVC005", "Service Administration RH", dirRH);
            srv5.setDescription("Service en charge de l'administration du personnel");
            serviceRepository.save(srv5);

            ServiceEntite srv6 = new ServiceEntite("SVC006", "Service Formation", dirRH);
            srv6.setDescription("Service en charge de la formation");
            serviceRepository.save(srv6);

            System.out.println("✓ Services créés avec succès");
        } else {
            System.out.println("⚠ Les services existent déjà");
        }
    }

    private void createSections() {
        if (sectionRepository.count() == 0) {
            System.out.println("Création des sections...");

            ServiceEntite srvInfo = serviceRepository.findByCode("SVC001").orElseThrow();
            ServiceEntite srvReseau = serviceRepository.findByCode("SVC002").orElseThrow();
            ServiceEntite srvCommercial = serviceRepository.findByCode("SVC003").orElseThrow();

            // Sections du service informatique
            Section sec1 = new Section("SEC001", "Développement", srvInfo);
            sec1.setDescription("Section développement logiciel");
            sectionRepository.save(sec1);

            Section sec2 = new Section("SEC002", "Infrastructure", srvInfo);
            sec2.setDescription("Section infrastructure technique");
            sectionRepository.save(sec2);

            Section sec3 = new Section("SEC003", "Support Technique", srvInfo);
            sec3.setDescription("Section support aux utilisateurs");
            sectionRepository.save(sec3);

            // Sections du service réseaux
            Section sec4 = new Section("SEC004", "Réseaux", srvReseau);
            sec4.setDescription("Section administration réseaux");
            sectionRepository.save(sec4);

            Section sec5 = new Section("SEC005", "Sécurité", srvReseau);
            sec5.setDescription("Section sécurité informatique");
            sectionRepository.save(sec5);

            // Sections du service commercial
            Section sec6 = new Section("SEC006", "Ventes B2B", srvCommercial);
            sec6.setDescription("Section ventes entreprises");
            sectionRepository.save(sec6);

            Section sec7 = new Section("SEC007", "Ventes B2C", srvCommercial);
            sec7.setDescription("Section ventes particuliers");
            sectionRepository.save(sec7);

            System.out.println("✓ Sections créées avec succès");
        } else {
            System.out.println("⚠ Les sections existent déjà");
        }
    }

    private void createUsers() {
        if (collaborateurRepository.count() == 0) {
            System.out.println("Création des utilisateurs...");

            // Récupérer les entités nécessaires
            Direction dirGen = directionRepository.findByCode("DIR001").orElseThrow();
            Direction dirTech = directionRepository.findByCode("DIR002").orElseThrow();
            Direction dirComm = directionRepository.findByCode("DIR003").orElseThrow();
            Direction dirRH = directionRepository.findByCode("DIR004").orElseThrow();

            ServiceEntite srvInfo = serviceRepository.findByCode("SVC001").orElseThrow();
            ServiceEntite srvComm = serviceRepository.findByCode("SVC003").orElseThrow();
            ServiceEntite srvRH = serviceRepository.findByCode("SVC005").orElseThrow();

            Section secDev = sectionRepository.findByCode("SEC001").orElseThrow();
            Section secB2B = sectionRepository.findByCode("SEC006").orElseThrow();

            // 1. ADMIN
            Collaborateur admin = new Collaborateur();
            admin.setNom("ADMIN");
            admin.setPrenoms("System");
            admin.setMatricule("ADMIN001");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setActif(true);
            admin.setDateEmbauche(LocalDate.now());
            admin.setPosteActuel("Administrateur Système");
            admin.setDirection(dirGen);
            collaborateurRepository.save(admin);
            System.out.println("  ✓ Admin créé: admin@example.com / admin123");

            // 2. DIRECTEUR TECHNIQUE
            Collaborateur directeurTech = new Collaborateur();
            directeurTech.setNom("TRAORE");
            directeurTech.setPrenoms("Issa Faycal");
            directeurTech.setMatricule("DIR001");
            directeurTech.setEmail("directeur.tech@entreprise.com");
            directeurTech.setPassword(passwordEncoder.encode("directeur123"));
            directeurTech.setRole(Role.DIRECTEUR);
            directeurTech.setActif(true);
            directeurTech.setDateEmbauche(LocalDate.now().minusYears(2));
            directeurTech.setPosteActuel("Directeur Technique");
            directeurTech.setDirection(dirTech);
            collaborateurRepository.save(directeurTech);
            System.out.println("  ✓ Directeur Technique créé: directeur.tech@entreprise.com / directeur123");

            // 3. DIRECTEUR COMMERCIAL
            Collaborateur directeurComm = new Collaborateur();
            directeurComm.setNom("DIALLO");
            directeurComm.setPrenoms("Amadou");
            directeurComm.setMatricule("DIR002");
            directeurComm.setEmail("directeur.comm@entreprise.com");
            directeurComm.setPassword(passwordEncoder.encode("directeur123"));
            directeurComm.setRole(Role.DIRECTEUR);
            directeurComm.setActif(true);
            directeurComm.setDateEmbauche(LocalDate.now().minusYears(3));
            directeurComm.setPosteActuel("Directeur Commercial");
            directeurComm.setDirection(dirComm);
            collaborateurRepository.save(directeurComm);
            System.out.println("  ✓ Directeur Commercial créé: directeur.comm@entreprise.com / directeur123");

            // 4. CHEF DE SERVICE INFORMATIQUE
            Collaborateur chefInfo = new Collaborateur();
            chefInfo.setNom("SANHAMA");
            chefInfo.setPrenoms("Sawadogo Affsafou");
            chefInfo.setMatricule("CH001");
            chefInfo.setEmail("chef.info@entreprise.com");
            chefInfo.setPassword(passwordEncoder.encode("chef123"));
            chefInfo.setRole(Role.CHEF_SERVICE);
            chefInfo.setActif(true);
            chefInfo.setDateEmbauche(LocalDate.now().minusYears(1));
            chefInfo.setPosteActuel("Chef de Service Informatique");
            chefInfo.setDirection(dirTech);
            chefInfo.setService(srvInfo);
            chefInfo.setResponsableDirect(directeurTech);
            chefInfo.setResponsableHierarchique(directeurTech);
            collaborateurRepository.save(chefInfo);
            System.out.println("  ✓ Chef Service Info créé: chef.info@entreprise.com / chef123");

            // 5. CHEF DE SECTION DÉVELOPPEMENT
            Collaborateur chefDev = new Collaborateur();
            chefDev.setNom("OUEDRAOGO");
            chefDev.setPrenoms("Jean");
            chefDev.setMatricule("SEC001");
            chefDev.setEmail("chef.dev@entreprise.com");
            chefDev.setPassword(passwordEncoder.encode("section123"));
            chefDev.setRole(Role.CHEF_SECTION);
            chefDev.setActif(true);
            chefDev.setDateEmbauche(LocalDate.now().minusMonths(6));
            chefDev.setPosteActuel("Chef de Section Développement");
            chefDev.setDirection(dirTech);
            chefDev.setService(srvInfo);
            chefDev.setSection(secDev);
            chefDev.setResponsableDirect(chefInfo);
            chefDev.setResponsableHierarchique(directeurTech);
            collaborateurRepository.save(chefDev);
            System.out.println("  ✓ Chef Section Développement créé: chef.dev@entreprise.com / section123");

            // 6. COLLABORATEUR (Développeur)
            Collaborateur dev = new Collaborateur();
            dev.setNom("KONE");
            dev.setPrenoms("Moussa");
            dev.setMatricule("DEV001");
            dev.setEmail("moussa.kone@entreprise.com");
            dev.setPassword(passwordEncoder.encode("dev123"));
            dev.setRole(Role.COLLABORATEUR);
            dev.setActif(true);
            dev.setDateEmbauche(LocalDate.now().minusMonths(3));
            dev.setPosteActuel("Développeur Full Stack");
            dev.setDirection(dirTech);
            dev.setService(srvInfo);
            dev.setSection(secDev);
            dev.setResponsableDirect(chefDev);
            dev.setResponsableHierarchique(chefInfo);
            collaborateurRepository.save(dev);
            System.out.println("  ✓ Développeur créé: moussa.kone@entreprise.com / dev123");

            // 7. COLLABORATEUR (Commercial B2B)
            Collaborateur commercial = new Collaborateur();
            commercial.setNom("SISSOKO");
            commercial.setPrenoms("Fatou");
            commercial.setMatricule("COM001");
            commercial.setEmail("fatou.sissoko@entreprise.com");
            commercial.setPassword(passwordEncoder.encode("com123"));
            commercial.setRole(Role.COLLABORATEUR);
            commercial.setActif(true);
            commercial.setDateEmbauche(LocalDate.now().minusMonths(8));
            commercial.setPosteActuel("Commercial B2B");
            commercial.setDirection(dirComm);
            commercial.setService(srvComm);
            commercial.setSection(secB2B);
            commercial.setResponsableDirect(directeurComm);
            commercial.setResponsableHierarchique(directeurComm);
            collaborateurRepository.save(commercial);
            System.out.println("  ✓ Commercial créé: fatou.sissoko@entreprise.com / com123");

            // 8. RESPONSABLE RH
            Collaborateur responsableRH = new Collaborateur();
            responsableRH.setNom("BARRY");
            responsableRH.setPrenoms("Aissata");
            responsableRH.setMatricule("RH001");
            responsableRH.setEmail("rh@entreprise.com");
            responsableRH.setPassword(passwordEncoder.encode("rh123"));
            responsableRH.setRole(Role.CHEF_SERVICE);
            responsableRH.setActif(true);
            responsableRH.setDateEmbauche(LocalDate.now().minusYears(4));
            responsableRH.setPosteActuel("Responsable RH");
            responsableRH.setDirection(dirRH);
            responsableRH.setService(srvRH);
            responsableRH.setResponsableDirect(responsableRH);
            responsableRH.setResponsableHierarchique(responsableRH);
            collaborateurRepository.save(responsableRH);
            System.out.println("  ✓ Responsable RH créé: rh@entreprise.com / rh123");

            // 9. DIRECTEUR RH (à créer après car responsableRH en dépend)
            Collaborateur directeurRH = new Collaborateur();
            directeurRH.setNom("TOURE");
            directeurRH.setPrenoms("Mamadou");
            directeurRH.setMatricule("DIR003");
            directeurRH.setEmail("directeur.rh@entreprise.com");
            directeurRH.setPassword(passwordEncoder.encode("directeur123"));
            directeurRH.setRole(Role.DIRECTEUR);
            directeurRH.setActif(true);
            directeurRH.setDateEmbauche(LocalDate.now().minusYears(5));
            directeurRH.setPosteActuel("Directeur RH");
            directeurRH.setDirection(dirRH);
            collaborateurRepository.save(directeurRH);
            System.out.println("  ✓ Directeur RH créé: directeur.rh@entreprise.com / directeur123");

            // Mettre à jour le responsable RH pour ajouter son responsable hiérarchique
            responsableRH.setResponsableDirect(directeurRH);
            responsableRH.setResponsableHierarchique(directeurRH);
            collaborateurRepository.save(responsableRH);

            System.out.println("✓ Tous les utilisateurs ont été créés avec succès");
        } else {
            System.out.println("⚠ Des utilisateurs existent déjà dans la base");
        }
    }
}
