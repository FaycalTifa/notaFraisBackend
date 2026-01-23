package com.example.notaFraisBackend.resource;


import com.example.notaFraisBackend.entities.ServiceEntite;
import com.example.notaFraisBackend.service.DirectionService;
import com.example.notaFraisBackend.service.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/services")
@CrossOrigin(origins = "http://localhost:4200")
public class ServiceResource {

    @Autowired
    private ServiceService serviceEntiteService;
    @Autowired
    private DirectionService directionService;

    private static final Logger logger = LoggerFactory.getLogger(ServiceService.class);


    @PostMapping
    public ResponseEntity<ServiceEntite> createService(@RequestBody ServiceEntite serviceEntite) {
        try {
            System.out.println("========PostMapping serviceEntite =========" + serviceEntite);
            ServiceEntite savedService = serviceEntiteService.save(serviceEntite);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedService);
        } catch (Exception e) {
            logger.error("Erreur création service: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ServiceEntite>> getAllServices() {
        System.out.println("=====================services========================");
        List<ServiceEntite> services = serviceEntiteService.findAll();
        System.out.println("=====================getAllServices========================");
        System.out.println(services);
        System.out.println("=====================getAllServices========================");
        return ResponseEntity.ok(services);
    }



    @GetMapping("/{id}")
    public ResponseEntity<ServiceEntite> getServiceById(@PathVariable Long id) {
        System.out.println("=====================ServiceEntite========================");
        try {
            System.out.println("===================== try ServiceEntite========================");
            ServiceEntite service = serviceEntiteService.findById(id);
            return ResponseEntity.ok(service);
        } catch (IllegalArgumentException e) {
            System.out.println("=====================catch ServiceEntite========================");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceEntite> updateService(@PathVariable Long id, @RequestBody ServiceEntite serviceDetails) {
        try {
            ServiceEntite updatedService = serviceEntiteService.update(id, serviceDetails);
            return ResponseEntity.ok(updatedService);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        try {
            serviceEntiteService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/direction/{directionId}")
    public ResponseEntity<List<ServiceEntite>> getServicesByDirection(@PathVariable Long directionId) {
        List<ServiceEntite> services = serviceEntiteService.findByDirectionId(directionId);
        return ResponseEntity.ok(services);
    }

}
