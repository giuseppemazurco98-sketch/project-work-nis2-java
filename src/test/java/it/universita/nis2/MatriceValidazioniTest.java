package it.universita.nis2;

import it.universita.nis2.model.Incident;
import it.universita.nis2.service.NotificationValidation;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Genera una matrice empirica direttamente eseguendo le validazioni applicative.
 * ACCETTATO / RIFIUTATO sono risultati osservati, non regole copiate a mano.
 */
class MatriceValidazioniTest {

    private record Caso(String campoOCondizione, Consumer<Incident> modifica) { }

    private static Incident valido() {
        Incident i = new Incident();
        i.title = "Interruzione simulata";
        i.description = "Interruzione simulata del servizio aziendale per test didattico.";
        i.knownAt = LocalDateTime.of(2026, 11, 10, 9, 0);
        i.category = "IS-3";
        i.entityType = "IMPORTANTE";
        i.usersAffected = 1;
        i.downtimeMinutes = 1;
        return i;
    }

    private static String esito(Incident i, String fase) {
        try {
            switch (fase) {
                case "PRENOTIFICA" -> NotificationValidation.validatePrenotifica(i);
                case "NOTIFICA" -> NotificationValidation.validateNotifica(i);
                case "FINALE" -> NotificationValidation.validateFinale(i);
                default -> throw new IllegalArgumentException("Fase sconosciuta: " + fase);
            }
            return "ACCETTATO";
        } catch (ResponseStatusException ex) {
            return "RIFIUTATO";
        }
    }

    @Test
    void generaMatriceDaValidazioniReali() throws IOException {
        List<String> fasi = List.of("PRENOTIFICA", "NOTIFICA", "FINALE");
        List<Caso> casi = List.of(
            new Caso("Dati validi (controllo)", i -> { }),
            new Caso("title assente", i -> i.title = null),
            new Caso("description assente", i -> i.description = null),
            new Caso("knownAt assente", i -> i.knownAt = null),
            new Caso("category assente", i -> i.category = null),
            new Caso("entityType assente", i -> i.entityType = null),
            new Caso("usersAffected negativo", i -> i.usersAffected = -1),
            new Caso("downtimeMinutes negativo", i -> i.downtimeMinutes = -1),
            new Caso("usersAffected uguale a zero", i -> i.usersAffected = 0),
            new Caso("downtimeMinutes uguale a zero", i -> i.downtimeMinutes = 0),
            new Caso("usersAffected e downtimeMinutes entrambi zero", i -> {
                i.usersAffected = 0;
                i.downtimeMinutes = 0;
            }),
            new Caso("description inferiore a 30 caratteri", i -> i.description = "Breve")
        );

        StringBuilder csv = new StringBuilder("Campo o condizione;PRENOTIFICA;NOTIFICA;FINALE\n");
        for (Caso caso : casi) {
            csv.append(caso.campoOCondizione());
            for (String fase : fasi) {
                Incident i = valido();
                caso.modifica().accept(i);
                csv.append(';').append(esito(i, fase));
            }
            csv.append('\n');
        }

        // Verifiche automatiche: impediscono di produrre un prospetto inattendibile.
        for (String fase : fasi) {
            assertEquals("ACCETTATO", esito(valido(), fase));
        }
        Incident privaDiCategoria = valido();
        privaDiCategoria.category = null;
        assertEquals("RIFIUTATO", esito(privaDiCategoria, "PRENOTIFICA"));
        Incident senzaImpatto = valido();
        senzaImpatto.usersAffected = 0;
        senzaImpatto.downtimeMinutes = 0;
        assertEquals("RIFIUTATO", esito(senzaImpatto, "NOTIFICA"));
        Incident descrizioneBreve = valido();
        descrizioneBreve.description = "Breve";
        assertEquals("RIFIUTATO", esito(descrizioneBreve, "FINALE"));

        Path destinazione = Path.of("target", "matrice-validazioni.csv");
        Files.createDirectories(destinazione.getParent());
        Files.writeString(destinazione, csv, StandardCharsets.UTF_8);
        System.out.println("Matrice generata da NotificationValidation: " + destinazione.toAbsolutePath());
    }
}
