package it.universita.nis2.service;

import it.universita.nis2.model.Incident;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class NotificationValidation {
    private NotificationValidation() {}

    public static void validateBase(Incident incident) {
        if (incident == null || incident.title == null || incident.title.isBlank()
                || incident.description == null || incident.description.isBlank()
                || incident.knownAt == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campi di base mancanti");
        }
        if (incident.usersAffected < 0 || incident.downtimeMinutes < 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Utenti coinvolti e minuti di interruzione non possono essere negativi"
            );
        }
    }

    public static void validatePrenotifica(Incident incident) {
        validateBase(incident);

        if (incident.category == null || incident.category.isBlank()
                || incident.entityType == null || incident.entityType.isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Pre-notifica: categoria ACN e tipo di soggetto obbligatori"
            );
        }
    }


    public static void validateNotifica(Incident incident) {
        validatePrenotifica(incident);

        if (incident.usersAffected == 0 && incident.downtimeMinutes == 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Notifica: indicare almeno un dato quantitativo di impatto"
            );
        }
    }

}
