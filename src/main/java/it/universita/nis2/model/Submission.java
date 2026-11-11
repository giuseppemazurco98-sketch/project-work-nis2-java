package it.universita.nis2.model;
import jakarta.persistence.*;
import java.time.*;
@Entity public class Submission {
@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
public Long incidentId;
public String phase;
public String categoryAtSubmission;
public LocalDateTime createdAt;
@Column(length=20000) public String snapshotJson;
public String sha256;
}
