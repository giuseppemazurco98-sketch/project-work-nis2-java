package it.universita.nis2.service;

import it.universita.nis2.model.Incident;

public class SignificativitaDidattica {

    public static final int SOGLIA_MINUTI = 60;

    public static boolean superaSoglia(Incident incidente) {
        return incidente != null
                && incidente.slaViolated
                && incidente.downtimeMinutes >= SOGLIA_MINUTI;
    }
}
