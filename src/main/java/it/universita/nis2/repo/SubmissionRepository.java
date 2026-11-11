package it.universita.nis2.repo;
import it.universita.nis2.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SubmissionRepository extends JpaRepository<Submission,Long> {java.util.List<Submission> findByIncidentIdOrderByCreatedAtAsc(Long incidentId);}
