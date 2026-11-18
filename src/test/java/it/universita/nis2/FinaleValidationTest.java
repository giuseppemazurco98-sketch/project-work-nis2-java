package it.universita.nis2;

import it.universita.nis2.model.Incident;
import it.universita.nis2.service.NotificationValidation;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FinaleValidationTest {

    @Test
    void bloccaRelazioneFinaleTroppoBreve() {
        Incident incidente = new Incident();
        incidente.title = "Incidente simulato";
        incidente.description = "Descrizione breve";
        incidente.knownAt = LocalDateTime.now();

        assertThrows(ResponseStatusException.class,
                () -> NotificationValidation.validateFinale(incidente));
    }
}
