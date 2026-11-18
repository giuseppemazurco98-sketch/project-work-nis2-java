package it.universita.nis2.config;

import it.universita.nis2.model.Category;
import it.universita.nis2.repo.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedData {

    @Bean
    CommandLineRunner seed(CategoryRepository r) {
        return args -> {
            String fonte = "Determinazione ACN 379907/2025, Allegati 3 e 4";

            r.save(new Category(
                "IS-1",
                "Perdita di riservatezza verso l'esterno di dati digitali "
                + "di proprieta' del soggetto NIS o sotto il suo controllo",
                "ENTRAMBI",
                fonte
            ));

            r.save(new Category(
                "IS-2",
                "Perdita di integrita' dei dati con impatto verso l'esterno",
                "ENTRAMBI",
                fonte
            ));

            r.save(new Category(
                "IS-3",
                "Violazione dei livelli di servizio attesi, "
                + "definiti secondo la misura DE.CM-01",
                "ENTRAMBI",
                fonte
            ));

            r.save(new Category(
                "IS-4",
                "Accesso non autorizzato o con abuso dei privilegi "
                + "a dati digitali del soggetto NIS o sotto il suo controllo",
                "ESSENZIALE",
                fonte
            ));
        };
    }
}
