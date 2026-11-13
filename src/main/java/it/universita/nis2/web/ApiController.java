package it.universita.nis2.web;
import it.universita.nis2.model.*;
import it.universita.nis2.repo.*;
import it.universita.nis2.service.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api") public class ApiController {
private final IncidentService svc;private final CategoryRepository categories;private final SubmissionRepository submissions;
public ApiController(IncidentService s,CategoryRepository c,SubmissionRepository sr){svc=s;categories=c;submissions=sr;}
@GetMapping("/categories") public List<Category> categories(){return categories.findAll();}
@GetMapping("/incidents") public List<Incident> all(){return svc.all();}
@PostMapping("/incidents") public Incident create(@Valid @RequestBody Incident i){return svc.create(i);}
@GetMapping("/incidents/{id}") public Incident get(@PathVariable Long id){return svc.get(id);}
@PutMapping("/incidents/{id}") public Incident update(@PathVariable Long id,@Valid @RequestBody Incident i){return svc.update(id,i);}
@GetMapping("/incidents/{id}/assessment") public Map<String,Object> assessment(@PathVariable Long id){return svc.assessment(id);}
@GetMapping("/incidents/{id}/submissions") public List<Submission> submissions(@PathVariable Long id){return svc.submissions(id);}
@PostMapping("/incidents/{id}/submissions/{phase}") public Submission submit(@PathVariable Long id,@PathVariable String phase){return svc.submit(id,phase);}
@GetMapping("/incidents/{id}/changes") public List<Reclassification> changes(@PathVariable Long id){return svc.changes(id);}
@PostMapping("/incidents/{id}/reclassify") public Reclassification reclassify(@PathVariable Long id,@RequestBody Map<String,String> input){return svc.reclassify(id,input.get("category"),input.get("reason"));}
@GetMapping(value="/submissions/{id}/json",produces="application/json")public ResponseEntity<String> json(@PathVariable Long id){Submission s=submissions.findById(id).orElseThrow();return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=notifica-"+id+".json").body(s.snapshotJson);}
}
