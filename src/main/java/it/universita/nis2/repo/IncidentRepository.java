package it.universita.nis2.repo;
import it.universita.nis2.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IncidentRepository extends JpaRepository<Incident,Long> {}
