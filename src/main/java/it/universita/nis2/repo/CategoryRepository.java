package it.universita.nis2.repo;
import it.universita.nis2.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoryRepository extends JpaRepository<Category,String> {}
