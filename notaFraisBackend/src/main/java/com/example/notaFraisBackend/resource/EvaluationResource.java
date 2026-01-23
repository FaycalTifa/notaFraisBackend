package com.example.notaFraisBackend.resource;


import com.example.notaFraisBackend.entities.Direction;
import com.example.notaFraisBackend.entities.Evaluation;
import com.example.notaFraisBackend.service.EvaluationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/evaluation")
@CrossOrigin(origins = "http://localhost:4200")
public class EvaluationResource {

    @Autowired
    private EvaluationService evaluationService;

    private static final Logger logger = LoggerFactory.getLogger(EvaluationResource.class);


    /**
     *
     * @param evaluation
     * @return
     */
    @PostMapping
    public ResponseEntity<Evaluation> createProduct(@RequestBody Evaluation evaluation) {
        logger.info("+++++++++++++ ajout evaluation en cours dans le service ++++++++++++");
        if (evaluation != null) {
            evaluation = evaluationService.save(evaluation);
            logger.info("========== save evaluation Resource réussi [Code: {}] ===============", HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(evaluation);
        } else {
            logger.warn("Requête invalide : banque est null [Code: {}]", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Evaluation>> findAll() {
        List<Evaluation> evaluation = evaluationService.findAll();
        logger.info("+++++++++++++ list evaluation en cours dans le service ++++++++++++");
        return ResponseEntity.ok(evaluation);
    }

    /**
     *
     * @param id
     * @param evaluation
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Evaluation> updateBanque(@PathVariable Long id, @RequestBody Evaluation evaluation) {
        logger.info("+++++++++++++ UPDATE Agence RESSOURCE++++++++++++");
        System.out.println("=========== UPDATE RESSOURCE +===============");
        Evaluation updatedEvaluation = evaluationService.update(id, evaluation);
        return new ResponseEntity<>(updatedEvaluation, HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @param evaluation
     * @return
     */
    @PutMapping("/deleteEvaluation/{id}")
    public ResponseEntity<Evaluation> delete(@PathVariable Long id, @RequestBody Evaluation evaluation) {
        logger.info("+++++++++++++ DELETE Direction RESSOURCE++++++++++++");
        Evaluation updatedEvaluation = evaluationService.delete(id, evaluation);
        return new ResponseEntity<>(updatedEvaluation, HttpStatus.OK);
    }

}
