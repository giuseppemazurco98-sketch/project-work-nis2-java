package it.universita.nis2;

import it.universita.nis2.model.Incident;
import it.universita.nis2.service.NotificationValidation;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationValidationTest {

    @Test
    void bloccaValoriNegativi() {
        Incident incidente = new Incident();
        incidente.title = "Interruzione simulata";
        incidente.description = "Descrizione dell'incidente simulato";
        incidente.knownAt = LocalDateTime.now();

        incidente.usersAffected = -1;
        assertThrows(ResponseStatusException.class,
                () -> NotificationValidation.validateBase(incidente));

        incidente.usersAffected = 10;
        incidente.downtimeMinutes = -5;
        assertThrows(ResponseStatusException.class,
                () -> NotificationValidation.validateBase(incidente));
    }
}
