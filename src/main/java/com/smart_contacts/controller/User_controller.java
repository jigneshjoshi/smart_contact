package com.smart_contacts.controller;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.core.sym.Name;
import com.smart_contacts.ENTITIES.Contact;
import com.smart_contacts.ENTITIES.User;
import com.smart_contacts.deo.Contactrepo;
import com.smart_contacts.deo.Userrepo;
import com.smart_contacts.halper.Message;



import java.util.Optional;

@Controller
@RequestMapping("/user")
public class User_controller {
	@Autowired
	private Userrepo userrepo;
	@Autowired
	private Contactrepo contactrepo;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	 // This method run for every method index, add_contact or etc.
    // Method for adding common data for response.
    
	 @ModelAttribute
	    public void addCommonData(Model model, Principal principal) {
	        String userName = principal.getName();
	        User user = userrepo.getUserByUserName(userName);
	        model.addAttribute("user", user);
	    }

	    @RequestMapping("/index")
	    public String dashBoard(Model model, Principal principal) {
	        model.addAttribute("title", "User Dashboard");
	        return "normal/user_dashboard";
	    }
	    // Open add form handler
	    @GetMapping("/add-contact")
	    
	    public String openAddContactForm(Model model) {
	        model.addAttribute("title", "Add Contact");
	        model.addAttribute("contact", new Contact());
	        return "normal/add_contact_form";
	    }
	    
	    @PostMapping("/process-contact")
	    public String processContact(@Valid @ModelAttribute Contact contact, @RequestParam("processImage") MultipartFile multipartFile, BindingResult bindingResult,
                Model model, Principal principal, HttpSession session) {

try {
if (bindingResult.hasErrors()) {
model.addAttribute("contact", contact);
return "normal/add_contact_form";
}

String userName = principal.getName();
User user = this.userrepo.getUserByUserName(userName);

// You can use throw exception on alert box
/*if (3>2){
throw new  Exception();
// goto catch block
}*/

// processing and uploading file
if (multipartFile.isEmpty()) {
//
System.out.println("File not Uploaded");
model.addAttribute("contact", contact);
session.setAttribute("message", new Message("Please Select a Photo", "alert-danger"));
return "normal/add_contact_form";

} else {
contact.setImage(multipartFile.getOriginalFilename());

// File save to any folder
/*String userDirectory = System.getProperty("user.dir");
String uploadDirectory = userDirectory + "\\uploadImg";
*/
// image save to static folder
File saveFile = new ClassPathResource("static/img/").getFile();

Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
System.out.println("File is Uploaded");
}
contact.setUser(user);

user.getContacts().add(contact);
this.userrepo.save(user);
System.out.println("Data: " + contact);
model.addAttribute("contact", new Contact());

/*Message Success*/
session.setAttribute("message", new Message("Contact added Successfully!! ", "alert-success"));

return "normal/add_contact_form";
} catch (Exception e) {
e.printStackTrace();
model.addAttribute("contact", contact);
/* Message Success */
session.setAttribute("message", new Message("Something went wrong " + e.getMessage(), "alert-danger"));
}

return "normal/add_contact_form";

}

// show_contact page
	    //per page 5=n
	    //current page =0
	    @GetMapping("/show-contacts/{page}")
	    public String show_contact(@PathVariable("page") Integer page, Model m,Principal principal) {
	    	m.addAttribute("title", "show contacts");	    
	    	String username= principal.getName();
	    	
	    	User user =this.userrepo.getUserByUserName(username);
	    	//List<Contact> contact=user.getContacts();
	    Pageable pageable = PageRequest.of(page, 3);
	    	Page<Contact> contact=this.contactrepo.findContactsbyUser(user.getId(),pageable);
	    	m.addAttribute("contact", contact);
	    	m.addAttribute("currentpage", page);
	    	m.addAttribute("totalpage", contact.getTotalPages());
	    	
	    	return "normal/show_contacts";
			
		}
	    
	    @RequestMapping("/{cid}/contact")
	    public String showContactDetails(@PathVariable("cid") Integer cid, Model model, Principal principal) {
	        // System.out.println("Cid:"+cid);
	        model.addAttribute("title", "Contact");

	        Optional<Contact> contactOptional = this.contactrepo.findById(cid);


	        if (contactOptional.isPresent()) {
	            Contact contact = contactOptional.get();

	            // get current user
	            String username = principal.getName();
	            User user = this.userrepo.getUserByUserName(username);

	            // show contact only current user
	            if (user.getId() == contact.getUser().getId()) {
	                model.addAttribute("contact", contact);
	            }
	        }

	        return "normal/contact_details";
	    }
	    @GetMapping("/profile")
	    public String yourprofile(Model m)
	    {
	    	m.addAttribute("title", "profile");
	    
	    	return "normal/profile_page";
	    }
	    
	    @PostMapping("/profile_img_user")
	    public String User_profile(Model m, @RequestParam("profileImage") MultipartFile multipartFile,Principal principal) {
	    	try {	// TODO: handle exception
			String username=principal.getName();
	    	User user=this.userrepo.getUserByUserName(username);
	    	
	    	File saveFile = new ClassPathResource("static/img/").getFile().getAbsoluteFile();
	    	Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
	    	Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	    	this.userrepo.save(user);
	    	return "normal/profile_page";
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	return "normal/profile_page";
			}
			
	    
	    @GetMapping("/delete/{cid}")
	    public String deleteContact(@PathVariable("cid") Integer cid, Model model, Principal principal, HttpSession session) {
	        Optional<Contact> contactOptional = this.contactrepo.findById(cid);
	        if (contactOptional.isPresent()) {
	            Contact contact = contactOptional.get();

	            // get current user
	            String username = principal.getName();
	            User user = this.userrepo.getUserByUserName(username);	
	           
	            // delete contact only current user
	            if (user.getId() == contact.getUser().getId()) {
	                this.contactrepo.delete(contact);

	                session.setAttribute("message", new Message("Contact deleted Successfully", "alert-success"));
	            }

	        }
	        return "redirect:/user/show-contacts/0";
	    }
	    
	    @PostMapping("/update/{cid}")
	    public String updateContact(@PathVariable("cid") Integer cid, Model model, Principal principal, HttpSession session) {
	        model.addAttribute("title", "Update Contact");
	        Contact contact = this.contactrepo.findById(cid).get();
	        model.addAttribute("contact", contact);
	        return "normal/update_form";
	    }
	   
	    @PostMapping("/process-update")
	    public String updateForm(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile multipartFile,
                Model model, Principal principal, HttpSession session) {
	    	try {
	            // Fetch old contact
	            Contact oldContact = this.contactrepo.findById((contact.getcId())).get();
	            if (!multipartFile.isEmpty()) {
	                // file rewrite
	                // At first delete old photo and update photo
	                // delete photo
	                File deleteFile = new ClassPathResource("static/img").getFile();
	                File oldFile = new File(deleteFile, oldContact.getImageUrl());
	                boolean isDelete = oldFile.delete();

	                // Update photo
	                File saveFile = new ClassPathResource("static/img/").getFile();
	                // rename file with currentTimeMillis
	                String filename = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

	                filename = System.currentTimeMillis() + filename.toLowerCase().replaceAll(" ", "-");
	                Path rootLocation = Paths.get(saveFile + File.separator);

	                Files.copy(multipartFile.getInputStream(), rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

	                contact.setImage(filename);

	            } else {
	                contact.setImage(oldContact.getImageUrl());
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        User user = this.userrepo.getUserByUserName(principal.getName());
	        contact.setUser(user);
	        this.contactrepo.save(contact);

	        session.setAttribute("message", new Message("Your contact is updated...", "alert-success"));

	        // redirect uses for URL not html file
	    return "redirect:/user/" + contact.getcId() + "/contact";
	    }
	    @GetMapping("/settings")
	    public String openSetting(Model model) {
	        model.addAttribute("title", "Settings");
	        return "normal/setting";
	    }
	    // change password handler
	    @PostMapping("/change-password")
	    public String changePassword(Model model, Principal principal, HttpSession session,
	                                 @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {

	        String username = principal.getName();
	        User currentUser = this.userrepo.getUserByUserName(username);

	        if (this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
	            currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
	            this.userrepo.save(currentUser);
	            session.setAttribute("message", new Message("Your password is successfully changed...", "alert-success"));

	        } else {
	            session.setAttribute("message", new Message("Your old password is wrong!!", "alert-danger"));
	            return "redirect:/user/settings";
	        }

	        return "redirect:/user/index";

	    }
 }
	   
	    
	  
