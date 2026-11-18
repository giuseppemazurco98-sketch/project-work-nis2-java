package it.universita.nis2;
import it.universita.nis2.model.Incident;
import it.universita.nis2.service.IncidentService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class IncidentServiceTest {
@Test void sogliaSopraESotto(){IncidentService service=mock(IncidentService.class,CALLS_REAL_METHODS);Incident incident=new Incident();incident.category="IS-3";incident.slaViolated=false;assertFalse(service.significant(incident));incident.slaViolated=true;assertTrue(service.significant(incident));}
@Test void categorieRiservatezzaEIntegrita(){IncidentService service=mock(IncidentService.class,CALLS_REAL_METHODS);Incident incident=new Incident();incident.category="IS-1";incident.externalDataLoss=true;assertTrue(service.significant(incident));incident.category="IS-2";assertFalse(service.significant(incident));incident.externalIntegrityLoss=true;assertTrue(service.significant(incident));}


    @Test
    void prenotificaRifiutaDatiIncompleti() {
        IncidentService service = mock(IncidentService.class, CALLS_REAL_METHODS);
        Incident incidente = new Incident();
        incidente.id = 1L;
        incidente.category = "IS-3";
        incidente.slaViolated = true;
        incidente.title = "";
        incidente.description = "Interruzione del servizio";
        doReturn(incidente).when(service).get(1L);
        doReturn(java.util.List.of()).when(service).submissions(1L);
        assertThrows(
            org.springframework.web.server.ResponseStatusException.class,
            () -> service.submit(1L, "PRENOTIFICA")
        );
    }

    @Test
    void notificaRifiutaDatiIncompleti() {
        IncidentService service = mock(IncidentService.class, CALLS_REAL_METHODS);
        Incident incidente = new Incident();
        incidente.id = 1L;
        incidente.category = "IS-3";
        incidente.slaViolated = true;
        incidente.title = "Interruzione del portale";
        incidente.description = "";
        incidente.knownAt = java.time.LocalDateTime.now();
        it.universita.nis2.model.Submission precedente =
            new it.universita.nis2.model.Submission();
        precedente.phase = "PRENOTIFICA";
        doReturn(incidente).when(service).get(1L);
        doReturn(java.util.List.of(precedente))
            .when(service).submissions(1L);
        org.springframework.web.server.ResponseStatusException errore =
            assertThrows(
                org.springframework.web.server.ResponseStatusException.class,
                () -> service.submit(1L, "NOTIFICA")
            );
        assertEquals("Campi di base mancanti", errore.getReason());
    }



    @Test
    void finaleRifiutaDescrizioneBreve() {
        IncidentService service = mock(IncidentService.class, CALLS_REAL_METHODS);

        Incident incidente = new Incident();
        incidente.id = 1L;
        incidente.category = "IS-3";
        incidente.slaViolated = true;
        incidente.title = "Interruzione del portale";
        incidente.description = "Guasto breve";
        incidente.knownAt = java.time.LocalDateTime.now();

        it.universita.nis2.model.Submission precedente =
            new it.universita.nis2.model.Submission();
        precedente.phase = "NOTIFICA";

        doReturn(incidente).when(service).get(1L);
        doReturn(java.util.List.of(precedente))
            .when(service).submissions(1L);

        org.springframework.web.server.ResponseStatusException errore =
            assertThrows(
                org.springframework.web.server.ResponseStatusException.class,
                () -> service.submit(1L, "FINALE")
            );

        assertTrue(errore.getReason().contains("almeno 30 caratteri"));
    }


    @Test
    void finaleContieneStoricoRiclassificazione() throws Exception {
        var incidenti = mock(it.universita.nis2.repo.IncidentRepository.class);
        var notifiche = mock(it.universita.nis2.repo.SubmissionRepository.class);
        var modifiche = mock(it.universita.nis2.repo.ReclassificationRepository.class);
        var categorie = mock(it.universita.nis2.repo.CategoryRepository.class);
        var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        var service = new IncidentService(incidenti, notifiche, modifiche, categorie, mapper);

        Incident incidente = new Incident();
        incidente.id = 99L;
        incidente.title = "Interruzione simulata autenticazione";
        incidente.description = "Interruzione simulata del servizio con successiva alterazione dei dati.";
        incidente.knownAt = java.time.LocalDateTime.of(2026, 11, 10, 9, 0);
        incidente.category = "IS-2";
        incidente.entityType = "IMPORTANTE";
        incidente.externalIntegrityLoss = true;

        var prenotifica = new it.universita.nis2.model.Submission();
        prenotifica.phase = "PRENOTIFICA";
        var notifica = new it.universita.nis2.model.Submission();
        notifica.phase = "NOTIFICA";

        var riclassificazione = new it.universita.nis2.model.Reclassification();
        riclassificazione.incidentId = 99L;
        riclassificazione.fromCategory = "IS-3";
        riclassificazione.toCategory = "IS-2";
        riclassificazione.reason = "Alterazione simulata dei dati verso l'esterno";
        riclassificazione.changedAt = java.time.LocalDateTime.of(2026, 11, 18, 10, 36);

        when(incidenti.findById(99L)).thenReturn(java.util.Optional.of(incidente));
        when(notifiche.findByIncidentIdOrderByCreatedAtAsc(99L))
            .thenReturn(java.util.List.of(prenotifica, notifica));
        when(modifiche.findByIncidentIdOrderByChangedAtAsc(99L))
            .thenReturn(java.util.List.of(riclassificazione));
        when(notifiche.save(any(it.universita.nis2.model.Submission.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        var finale = service.submit(99L, "FINALE");
        var json = mapper.readTree(finale.snapshotJson);
        var storico = json.path("storicoRiclassificazioni");

        assertEquals("IS-2", json.path("classificazione").asText());
        assertEquals(1, storico.size());
        assertEquals("IS-3", storico.get(0).path("categoriaPrecedente").asText());
        assertEquals("IS-2", storico.get(0).path("categoriaNuova").asText());
        assertEquals("2026-11-18T10:36", storico.get(0).path("dataRiclassificazione").asText());
        assertEquals(riclassificazione.reason, storico.get(0).path("motivazione").asText());
    }

}
