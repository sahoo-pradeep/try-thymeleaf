package sahoo.projects.trythymeleaf.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import sahoo.projects.trythymeleaf.exception.ResourceAlreadyExistsException;
import sahoo.projects.trythymeleaf.exception.ResourceNotFoundException;
import sahoo.projects.trythymeleaf.model.Contact;
import sahoo.projects.trythymeleaf.service.ContactService;

@Controller
@Slf4j
public class ContactController {

  private final ContactService contactService;

  @Autowired
  public ContactController(ContactService contactService) {
    this.contactService = contactService;
  }

  @Value("${index.title}")
  private String title;

  @GetMapping({"/", "/index"})
  public String index(Model model) {
    log.info("Inside index page");
    model.addAttribute("title", title);
    return "index";
  }

  @GetMapping("/contact")
  public String findAll(Model model) {
    log.info("Find all contacts");
    List<Contact> contacts = contactService.findAll();
    model.addAttribute("contacts", contacts);
    return "contact-list";
  }

  @GetMapping("/contact/{contactId}")
  public String findById(Model model, @PathVariable Long contactId) {
    log.info("Find contact by id: {}", contactId);
    Contact contact = null;
    try {
      contact = contactService.findById(contactId);
    } catch (ResourceNotFoundException e) {
      String errorMessage = e.getMessage();
      log.error(errorMessage);
      model.addAttribute("errorMessage", errorMessage);
    }
    model.addAttribute("contact", contact);
    return "contact";
  }

  @GetMapping("/contact/add")
  public String showAddContact(Model model) {
    Contact contact = new Contact();
    model.addAttribute("add", true);
    model.addAttribute("contact", contact);
    return "contact-edit";
  }

  @PostMapping("/contact/add")
  public String addContact(Model model, @ModelAttribute("contact") Contact contact) {
    try {
      Contact newContact = contactService.save(contact);
      return "redirect:/contact/" + newContact.getId();
    } catch (Exception e) {
      String errorMessage = e.getMessage();
      log.error(errorMessage);
      model.addAttribute("errorMessage", errorMessage);
      model.addAttribute("add", true);
      return "contact-edit";
    }
  }

  @GetMapping("/contact/{contactId}/edit")
  public String showEditContact(Model model, @PathVariable Long contactId) {
    Contact contact = null;
    try {
      contact = contactService.findById(contactId);
    } catch (ResourceNotFoundException e) {
      String errorMessage = e.getMessage();
      log.error(errorMessage);
      model.addAttribute("errorMessage", errorMessage);
    }

    model.addAttribute("add", false);
    model.addAttribute("contact", contact);
    return "contact-edit";
  }

  @PostMapping("/contact/{contactId}/edit")
  public String updateContact(Model model, @PathVariable Long contactId,
      @ModelAttribute("contact") Contact contact) {
    try {
      contact.setId(contactId);
      contactService.update(contact);
      return "redirect:/contact/" + contactId;
    } catch (ResourceNotFoundException e) {
      String errorMessage = e.getMessage();
      log.error(errorMessage);
      model.addAttribute("errorMessage", errorMessage);
      model.addAttribute("add", false);
      return "contact-edit";
    }
  }

  @GetMapping("/contact/{contactId}/delete")
  public String showDeleteContact(Model model, @PathVariable Long contactId) {
    Contact contact = null;
    try {
      contact = contactService.findById(contactId);
    } catch (ResourceNotFoundException e) {
      String errorMessage = e.getMessage();
      log.error(errorMessage);
      model.addAttribute("errorMessage", errorMessage);
    }
    model.addAttribute("allowDelete", true);
    model.addAttribute("contact", contact);
    return "contact";
  }

  @PostMapping("/contact/{contactId}/delete")
  public String deleteContact(Model model, @PathVariable Long contactId) {
    try {
      contactService.deleteById(contactId);
      return "redirect:/contact";
    } catch (ResourceNotFoundException e) {
      String errorMessage = e.getMessage();
      log.error(errorMessage);
      model.addAttribute("errorMessage", errorMessage);
      return "contact";
    }
  }
}
