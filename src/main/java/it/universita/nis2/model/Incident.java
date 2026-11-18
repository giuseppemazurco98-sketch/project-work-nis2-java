package it.universita.nis2.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.*;
@Entity public class Incident {
@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
@NotBlank public String title;
@NotBlank @Column(length=4000) public String description;
@NotNull public LocalDateTime knownAt;
@NotNull public String entityType="IMPORTANTE";
@NotNull public String category="IS-3";
public int usersAffected;
public int downtimeMinutes;
public boolean externalDataLoss;
public boolean externalIntegrityLoss;
public boolean slaViolated;
public boolean unauthorizedAccess;
public String status="BOZZA";
public LocalDateTime createdAt=LocalDateTime.now();
public LocalDateTime updatedAt=LocalDateTime.now();
}
