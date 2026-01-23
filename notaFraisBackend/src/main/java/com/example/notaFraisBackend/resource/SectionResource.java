package com.example.notaFraisBackend.resource;


import com.example.notaFraisBackend.entities.Direction;
import com.example.notaFraisBackend.entities.Section;
import com.example.notaFraisBackend.entities.ServiceEntite;
import com.example.notaFraisBackend.service.SectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
@CrossOrigin(origins = "http://localhost:4200")
public class SectionResource {

    @Autowired
    private SectionService sectionService;

    private static final Logger logger = LoggerFactory.getLogger(SectionService.class);
    private Long serviceId;


    @PostMapping
    public ResponseEntity<Section> createSection(@RequestBody Section section) {
        try {
            System.out.println("======== PostMapping Section =========" + section);
            Section savedSection = sectionService.save(section);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSection);
        } catch (Exception e) {
            logger.error("Erreur création Section: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Section>> getAllSections() {
        List<Section> sections = sectionService.findAll();
        return ResponseEntity.ok(sections);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Section> getSectionById(@PathVariable Long id) {
        try {
            Section section = sectionService.findById(id);
            return ResponseEntity.ok(section);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Section> updateSection(@PathVariable Long id, @RequestBody Section sectionDetails) {
        try {
            Section updatedSection = sectionService.update(id, sectionDetails);
            return ResponseEntity.ok(updatedSection);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        try {
            sectionService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }


 /*   @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<Section>> getSectionsByService(@PathVariable Long serviceI) {
        this.serviceId = serviceId;
        List<Section> sections = sectionService.findByServiceId(serviceId);
        return ResponseEntity.ok(sections);
    }*/

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<Section>> getSectionsByService(@PathVariable Long serviceId) {
        this.serviceId = serviceId;
        List<Section> sections = sectionService.findByServiceId(serviceId);
        return ResponseEntity.ok(sections);
    }
}
