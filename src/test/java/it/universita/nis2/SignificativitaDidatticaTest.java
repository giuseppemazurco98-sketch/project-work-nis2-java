package it.universita.nis2;

import it.universita.nis2.model.Incident;
import it.universita.nis2.service.SignificativitaDidattica;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignificativitaDidatticaTest {

    @Test
    void sottoSoglia59Minuti() {
        Incident incidente = new Incident();
        incidente.slaViolated = true;
        incidente.downtimeMinutes = 59;

        assertFalse(SignificativitaDidattica.superaSoglia(incidente));
    }

    @Test
    void sopraSoglia61Minuti() {
        Incident incidente = new Incident();
        incidente.slaViolated = true;
        incidente.downtimeMinutes = 61;

        assertTrue(SignificativitaDidattica.superaSoglia(incidente));
    }
}
