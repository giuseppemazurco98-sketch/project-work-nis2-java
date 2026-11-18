package it.universita.nis2;

import it.universita.nis2.model.Incident;
import it.universita.nis2.service.NotificationValidation;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PrenotificaValidationTest {

    @Test
    void bloccaPrenotificaSenzaCategoria() {
        Incident incidente = new Incident();
        incidente.title = "Incidente simulato";
        incidente.description = "Descrizione dell'incidente";
        incidente.knownAt = LocalDateTime.now();
        incidente.category = "";

        assertThrows(ResponseStatusException.class,
                () -> NotificationValidation.validatePrenotifica(incidente));
    }
}
