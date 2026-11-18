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
    }
}
