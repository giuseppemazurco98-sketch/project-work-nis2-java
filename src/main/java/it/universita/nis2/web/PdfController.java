package it.universita.nis2.web;
import it.universita.nis2.repo.SubmissionRepository;
import it.universita.nis2.model.Submission;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.*;
@RestController public class PdfController {
private final SubmissionRepository repo;public PdfController(SubmissionRepository r){repo=r;}
@GetMapping(value="/api/submissions/{id}/pdf",produces="application/pdf") public ResponseEntity<byte[]> pdf(@PathVariable Long id)throws IOException{
Submission s=repo.findById(id).orElseThrow();try(PDDocument doc=new PDDocument();ByteArrayOutputStream out=new ByteArrayOutputStream()){
PDPage page=new PDPage();doc.addPage(page);try(PDPageContentStream cs=new PDPageContentStream(doc,page)){
cs.beginText();cs.setFont(PDType1Font.HELVETICA_BOLD,15);cs.newLineAtOffset(50,760);cs.showText("NOTIFICA INCIDENTE - SIMULAZIONE");cs.setFont(PDType1Font.HELVETICA,10);cs.newLineAtOffset(0,-28);
List<String> lines=List.of("Identificativo documento: "+s.id,"Fase: "+s.phase,"Incidente: "+s.incidentId,"Categoria al momento dell'invio: "+s.categoryAtSubmission,"Congelato il: "+s.createdAt,"SHA-256: "+s.sha256.substring(0,32),"","DOCUMENTO DIDATTICO - NON TRASMESSO AL CSIRT ITALIA","","Contenuto integrale disponibile nel documento JSON allegato.");for(String l:lines){cs.showText(l);cs.newLineAtOffset(0,-18);}cs.endText();}
doc.save(out);return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=notifica-"+id+".pdf").body(out.toByteArray());}}
}
