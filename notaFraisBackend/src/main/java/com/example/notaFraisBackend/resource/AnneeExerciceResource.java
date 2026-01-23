package com.example.notaFraisBackend.resource;

import com.example.notaFraisBackend.entities.AnneeExercice;
import com.example.notaFraisBackend.service.AnneeExerciceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/annee-exercice")
@CrossOrigin(origins = "http://localhost:4200")
public class AnneeExerciceResource {


    private final AnneeExerciceService service;

    public AnneeExerciceResource(AnneeExerciceService service) {
        this.service = service;
    }

    @GetMapping
    public List<AnneeExercice> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AnneeExercice getById(@PathVariable Long id) {
        return service.getById(id).orElse(null);
    }

    @PostMapping
    public AnneeExercice create(@RequestBody AnneeExercice anneeExercice) {
        System.out.println("================PostMapping====================");
        System.out.println(anneeExercice);
        System.out.println("====================================");
        return service.save(anneeExercice);
    }

    @PutMapping("/{id}")
    public AnneeExercice update(@PathVariable Long id, @RequestBody AnneeExercice anneeExercice) {
        return service.update(id, anneeExercice);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
