package it.universita.nis2;

import it.universita.nis2.model.Incident;
import it.universita.nis2.service.NotificationValidation;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificaValidationTest {

    @Test
    void bloccaNotificaSenzaDatiImpatto() {
        Incident incidente = new Incident();
        incidente.title = "Incidente simulato";
        incidente.description = "Interruzione del servizio";
        incidente.knownAt = LocalDateTime.now();
        incidente.category = "IS-3";
        incidente.entityType = "IMPORTANTE";
        incidente.usersAffected = 0;
        incidente.downtimeMinutes = 0;

        assertThrows(ResponseStatusException.class,
                () -> NotificationValidation.validateNotifica(incidente));
    }
}
