package sahoo.projects.trythymeleaf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sahoo.projects.trythymeleaf.exception.BadResourceException;
import sahoo.projects.trythymeleaf.exception.ResourceAlreadyExistsException;
import sahoo.projects.trythymeleaf.exception.ResourceNotFoundException;
import sahoo.projects.trythymeleaf.model.Contact;
import sahoo.projects.trythymeleaf.repository.ContactRepository;
import sahoo.projects.trythymeleaf.service.ContactService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

  private ContactRepository contactRepository;

  @Autowired
  public ContactServiceImpl(ContactRepository contactRepository) {
    this.contactRepository = contactRepository;
  }

  @Override
  public Contact findById(Long id) {
    return contactRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Cannot find contact with id: " + id));
  }

  @Override
  public List<Contact> findAll() {
    return new ArrayList<>(contactRepository.findAll());
  }

  @Override
  public Contact save(Contact contact) {
    if (contact.getName().isEmpty()) {
      throw new BadResourceException("Contact Name is empty");
    }

    if (contact.getId() != null && contactRepository.existsById(contact.getId())) {
      throw new ResourceAlreadyExistsException(
          "Contact already exists with id: " + contact.getId());
    }

    return contactRepository.save(contact);
  }

  @Override
  public Contact update(Contact contact) {
    if (!contactRepository.existsById(contact.getId())) {
      throw new ResourceNotFoundException("Cannot find contact with id: " + contact.getId());
    }

    return contactRepository.save(contact);
  }

  @Override
  public void deleteById(Long id) {
    if (!contactRepository.existsById(id)) {
      throw new ResourceNotFoundException("Cannot find contact with id: " + id);
    }

    contactRepository.deleteById(id);
  }
}
