package edu.swe307.grup2;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GraphController {
    private final GraphService graph;
    public GraphController(GraphService graph) { this.graph = graph; }

    @GetMapping("/api/status")
    public Map<String, Object> status() {
        return Map.of("group", 2, "column", "Col-2", "java", System.getProperty("java.version"));
    }

    @GetMapping(value = "/api/plot", produces = "image/png")
    public ResponseEntity<?> plot() {
        try {
            return ResponseEntity.ok().header("Cache-Control", "no-store").body(graph.nextPlot());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(503).header("Content-Type", "text/plain; charset=UTF-8")
                .body("Grafik oluşturulamadı. MongoDB bağlantısını, measurements verilerini ve FastR kurulumunu kontrol edin. Ayrıntı: " + ex.getMessage());
        }
    }
}
