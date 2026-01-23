package com.example.notaFraisBackend.service;

import com.example.notaFraisBackend.entities.AnneeExercice;
import com.example.notaFraisBackend.repository.AnneeExerciceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnneeExerciceService {

    private final AnneeExerciceRepository repository;

    public AnneeExerciceService(AnneeExerciceRepository repository) {
        this.repository = repository;
    }

    public List<AnneeExercice> getAll() {
        return repository.findAll();
    }

    public Optional<AnneeExercice> getById(Long id) {
        return repository.findById(id);
    }

    public AnneeExercice save(AnneeExercice anneeExercice) {
        System.out.println("================serv====================");
        System.out.println(anneeExercice);
        System.out.println("====================================");
        return repository.save(anneeExercice);
    }

    public AnneeExercice update(Long id, AnneeExercice newData) {
        return repository.findById(id).map(existing -> {
            existing.setAnnee(newData.getAnnee());
            existing.setActived(newData.getActived());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("AnneeExercice non trouvée"));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
