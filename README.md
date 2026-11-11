# NIS2 Notifiche — Project Work 29

Prototipo didattico **Java 21 / Spring Boot 3.5.16 / REST / Spring Data JPA / H2 / HTML+JavaScript**.

## Come avviare

1. Installare **Java JDK 21** e **Apache Maven**.
2. Aprire il terminale nella cartella contenente `pom.xml`.
3. Eseguire `mvn spring-boot:run`.
4. Aprire **http://localhost:8080**.
5. Per i test: `mvn test`; per creare il JAR: `mvn clean package` poi `java -jar target/nis2-notifiche-0.1.0.jar`.

Il database persistente H2 viene creato nella cartella `data/`. Le dipendenze Maven richiedono accesso a Maven Central alla prima esecuzione.

## Funzionalità incluse

- Creazione, modifica ed elenco incidenti e REST API.
- Categorie iniziali IS-1, IS-2, IS-3 e IS-4 (quest'ultima solo per essenziali).
- Valutazione **dimostrativa** della significatività e date teoriche +24h, +72h, +1 mese.
- Fasi PRENOTIFICA → NOTIFICA → FINALE; snapshot JSON immutabili e impronta SHA-256.
- Generazione PDF di ricevuta sintetica e download JSON integrale.
- Riclassificazione tracciata senza modificare gli snapshot esistenti.
- Alcune validazioni e test unitari della soglia dimostrativa.

## Limitazioni da risolvere prima della consegna definitiva

- **Non è un sistema ufficiale e non invia nulla al CSIRT Italia.**
- Il modello ACN è **parziale**: aggiornare da copia ufficiale della determinazione 379907/2025, allegati 3 e 4. La descrizione IS-4 è volutamente provvisoria; implementare versionamento della tassonomia, caricamento tramite migrazioni e tabella di discrepanze.
- La regola di significatività è **semplificata** per test di sopra/sotto-soglia; sostituirla con criteri verificati per il settore e il tipo di soggetto. Utenti e minuti di interruzione sono già raccolti, ma non fungono da soglia legale.
- Completarne i campi obbligatori differenziati, test per ogni fase, report PDF integrale (il PDF attuale è una **ricevuta**, non l'intera notifica), XML strutturato, audit log e versione dei documenti.
- Aggiungere autenticazione, autorizzazione, CSRF, protezione dati, backup, cifratura e database di produzione prima di trattare dati reali. L'interfaccia è una demo locale non protetta.
- Le scadenze rappresentano calcoli teorici dalla data/ora indicata: verificare termini e decorrenze applicabili prima dell'uso reale.

## API principali

`GET /api/categories`, `GET|POST /api/incidents`, `GET|PUT /api/incidents/{id}`, `GET /api/incidents/{id}/assessment`, `POST /api/incidents/{id}/submissions/{phase}`, `POST /api/incidents/{id}/reclassify`, `GET /api/incidents/{id}/submissions`, `GET /api/submissions/{id}/json`, `GET /api/submissions/{id}/pdf`.

## Fonti normative di partenza

- D.Lgs. 138/2024, art. 25.
- Determinazione ACN n. 379907/2025, allegati 3–4 (riferimento per il progetto NIS2).
- Determina ACN 9 febbraio 2026, G.U. 17 febbraio 2026, sulla tassonomia ai sensi della legge 90/2024: https://www.gazzettaufficiale.it/atto/serie_generale/caricaArticoloDefault/originario?atto.codiceRedazionale=26A00713&atto.dataPubblicazioneGazzetta=2026-02-17&atto.tipoProvvedimento=DETERMINA

**Nota:** la determina di febbraio 2026 e la determinazione NIS2 non vanno scambiate come fonti identiche.
