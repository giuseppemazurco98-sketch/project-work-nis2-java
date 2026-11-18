package it.universita.nis2.config;

import it.universita.nis2.model.Category;
import it.universita.nis2.repo.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SeedData {

    private static List<String> parseCsv(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (quoted && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {
                    value.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (ch == ',' && !quoted) {
                fields.add(value.toString());
                value.setLength(0);
            } else {
                value.append(ch);
            }
        }
        if (quoted) {
            throw new IllegalArgumentException("Virgolette CSV non chiuse");
        }
        fields.add(value.toString());
        return fields;
    }

    @Bean
    CommandLineRunner seed(CategoryRepository repository) {
        return args -> {
            Path file = Path.of("config", "categorie-acn.csv");
            List<String> lines = Files.readAllLines(
                    file, StandardCharsets.UTF_8);

            if (lines.isEmpty() || !lines.get(0).equals(
                    "code,description,applicableTo,source")) {
                throw new IllegalArgumentException(
                        "Intestazione CSV non valida");
            }

            for (int n = 1; n < lines.size(); n++) {
                List<String> fields = parseCsv(lines.get(n));
                if (fields.size() != 4
                        || fields.stream().anyMatch(String::isBlank)) {
                    throw new IllegalArgumentException(
                            "CSV non valido alla riga " + (n + 1));
                }
                repository.save(new Category(
                        fields.get(0),
                        fields.get(1),
                        fields.get(2),
                        fields.get(3)));
            }
        };
    }
}
