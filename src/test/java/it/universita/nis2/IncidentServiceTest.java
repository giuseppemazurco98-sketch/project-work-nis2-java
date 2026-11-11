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

}
