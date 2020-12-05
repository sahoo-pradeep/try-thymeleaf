package sahoo.projects.trythymeleaf.service;

import sahoo.projects.trythymeleaf.model.Contact;

import java.util.List;

public interface ContactService {

  Contact findById(Long id);

  List<Contact> findAll();

  Contact save(Contact contact);

  Contact update(Contact contact);

  void deleteById(Long id);
}
