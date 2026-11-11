package it.universita.nis2.model;
import jakarta.persistence.*;
@Entity public class Category {
@Id public String code;
@Column(length=3000) public String description;
public String applicableTo;
public String source;
protected Category(){}
public Category(String a,String b,String c,String d){code=a;description=b;applicableTo=c;source=d;}
}
