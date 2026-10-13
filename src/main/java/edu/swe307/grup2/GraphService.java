package edu.swe307.grup2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.PreDestroy;
import org.bson.Document;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class GraphService {
    private final MongoTemplate mongo;
    private Context context;
    private Value plotFunction;
    private int index;
    private long lastUpdate;
    private byte[] currentPlot;

    public GraphService(MongoTemplate mongo) { this.mongo = mongo; }

    // Browser requests are serialized because a FastR context must not be used concurrently.
    public synchronized byte[] nextPlot() {
        long now = System.nanoTime();
        if (currentPlot != null && (index == 100 || now - lastUpdate < 1_000_000_000L)) {
            return currentPlot;
        }

        //ÖNEMLİ KISIM 1(veri bulma + number kontrolü)
        Document row = mongo.findOne(Query.query(Criteria.where("index").is(index)), Document.class, "measurements");
        if (row == null || !(row.get("value") instanceof Number)) {
            throw new IllegalStateException("measurements içinde index=" + index + " için sayısal value bulunamadı.");
        }

        //ÖNEMLİ KISIM 2
        double value = ((Number) row.get("value")).doubleValue();
        if (!Double.isFinite(value)) throw new IllegalStateException("Veri sonlu bir sayı olmalı.");
        Path output = null;
        try {
            //ÖNEMLİ KISIM 3
            if (context == null) initializeR();
            output = Files.createTempFile("swe307-group2-", ".png");

            //elde edilen R fonks java tarafından .ağırılıyor(2)

            plotFunction.execute(index, value, output.toString());
            currentPlot = Files.readAllBytes(output);
            index++;
            lastUpdate = System.nanoTime();
            return currentPlot;
        } catch (IOException ex) {
            throw new IllegalStateException("Grafik dosyası okunamadı.", ex);
        } finally {
            if (output != null) {
                try { Files.deleteIfExists(output); } catch (IOException ignored) { }
            }
        }
    }
//GraalVM :
    private void initializeR() throws IOException {
        Context candidate = Context.newBuilder("R").allowAllAccess(true).build();
        try (var input = new ClassPathResource("r/plot.R").getInputStream()) {
            //R değerlendirmesi(1)
            Value function = candidate.eval("R", new String(input.readAllBytes(), StandardCharsets.UTF_8));
            context = candidate;
            plotFunction = function;
        } catch (IOException | RuntimeException ex) {
            candidate.close();
            throw ex;
        }
    }

    @PreDestroy
    public synchronized void close() { if (context != null) context.close(); }
}
