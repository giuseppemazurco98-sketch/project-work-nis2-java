package it.universita.nis2.config;
import it.universita.nis2.model.Category;
import it.universita.nis2.repo.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
@Configuration public class SeedData {
@Bean CommandLineRunner seed(CategoryRepository r){return args->{if(r.count()==0){String s="ACN Determinazione 379907/2025 - Allegati 3/4; descrizioni sintetiche da verificare sul documento ufficiale";r.save(new Category("IS-1","Perdita di riservatezza verso l'esterno di dati digitali","ENTRAMBI",s));r.save(new Category("IS-2","Perdita di integrità dei dati con impatto verso l'esterno","ENTRAMBI",s));r.save(new Category("IS-3","Violazione dei livelli di servizio attesi","ENTRAMBI",s));r.save(new Category("IS-4","Ulteriore fattispecie per soggetti essenziali: verificare esatta definizione nell'Allegato 4","ESSENZIALE",s));}};}
}
