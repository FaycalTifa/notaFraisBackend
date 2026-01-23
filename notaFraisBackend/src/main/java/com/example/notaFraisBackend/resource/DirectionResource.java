package com.example.notaFraisBackend.resource;

import com.example.notaFraisBackend.entities.Direction;
import com.example.notaFraisBackend.entities.ServiceEntite;
import com.example.notaFraisBackend.service.DirectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/direction")
public class DirectionResource {

    @Autowired
    private DirectionService directionService;

    private static final Logger logger = LoggerFactory.getLogger(DirectionResource.class);


    /**
     *
     * @param direction
     * @return
     */
    @PostMapping
    public ResponseEntity<Direction> createProduct(@RequestBody Direction direction) {
        logger.info("+++++++++++++ ajout direction en cours dans le service ++++++++++++");
        if (direction != null) {
            direction = directionService.save(direction);
            logger.info("========== save direction Resource réussi [Code: {}] ===============", HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(direction);
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
    public ResponseEntity<List<Direction>> findAll() {
        List<Direction> directions = directionService.findAll();
        logger.info("+++++++++++++ list Direction en cours dans le service ++++++++++++");
        return ResponseEntity.ok(directions);
    }

    /**
     *
     * @param id
     * @param direction
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Direction> updateBanque(@PathVariable Long id, @RequestBody Direction direction) {
        logger.info("+++++++++++++ UPDATE Agence RESSOURCE++++++++++++");
        System.out.println("=========== UPDATE RESSOURCE +===============");
        Direction updatedDirection = directionService.update(id, direction);
        return new ResponseEntity<>(updatedDirection, HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @param direction
     * @return
     */
    @PutMapping("/deleteAgence/{id}")
    public ResponseEntity<Direction> delete(@PathVariable Long id, @RequestBody Direction direction) {
        logger.info("+++++++++++++ DELETE Direction RESSOURCE++++++++++++");
        Direction updatedDirection = directionService.delete(id, direction);
        return new ResponseEntity<>(updatedDirection, HttpStatus.OK);
    }


}
