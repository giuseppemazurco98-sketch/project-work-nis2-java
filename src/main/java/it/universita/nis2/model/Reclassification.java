package it.universita.nis2.model;
import jakarta.persistence.*;
import java.time.*;
@Entity public class Reclassification {
@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
public Long incidentId; public String fromCategory; public String toCategory;
@Column(length=3000) public String reason;
public LocalDateTime changedAt;
}
