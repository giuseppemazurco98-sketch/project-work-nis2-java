package it.universita.nis2.repo;
import it.universita.nis2.model.Reclassification;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ReclassificationRepository extends JpaRepository<Reclassification,Long> {java.util.List<Reclassification> findByIncidentIdOrderByChangedAtAsc(Long incidentId);}
