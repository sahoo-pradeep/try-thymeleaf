package sahoo.projects.trythymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sahoo.projects.trythymeleaf.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

}
