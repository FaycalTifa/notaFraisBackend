package com.example.notaFraisBackend.resource;


import com.example.notaFraisBackend.entities.Agent;
import com.example.notaFraisBackend.service.AgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
@CrossOrigin(origins = "http://localhost:4200")
public class AgentResource {

    @Autowired
    private AgentService agentService;

    private static final Logger logger = LoggerFactory.getLogger(AgentResource.class);

    @PostMapping
    public ResponseEntity<Agent> createAgent(@RequestBody Agent agent) {
        try {
            Agent savedAgent = agentService.save(agent);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAgent);
        } catch (Exception e) {
            logger.error("Erreur création agent: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Agent>> getAllAgents() {
        List<Agent> agents = agentService.findAll();
        return ResponseEntity.ok(agents);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Agent> getAgentById(@PathVariable Long id) {
        try {
            Agent agent = agentService.findById(id);
            return ResponseEntity.ok(agent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agent> updateAgent(@PathVariable Long id, @RequestBody Agent agentDetails) {
        try {
            Agent updatedAgent = agentService.update(id, agentDetails);
            return ResponseEntity.ok(updatedAgent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        try {
            agentService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /*@GetMapping("/section/{sectionId}")
    public ResponseEntity<List<Agent>> getAgentsBySection(@PathVariable Long sectionId) {
        List<Agent> agents = agentService.findBySectionId(sectionId);
        return ResponseEntity.ok(agents);
    }*/

    // Vérifiez que l'URL correspond exactement
    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<Agent>> getAgentsBySection(@PathVariable Long sectionId) {
        try {
            List<Agent> agents = agentService.findBySectionId(sectionId);
            return ResponseEntity.ok(agents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
