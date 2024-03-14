package com.smart_contacts.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart_contacts.ENTITIES.Contact;
import com.smart_contacts.ENTITIES.User;
import com.smart_contacts.deo.Contactrepo;
import com.smart_contacts.deo.Userrepo;

@RestController
public class Searchcontoller {
	
	@Autowired
	 private Contactrepo contactrepo;
	@Autowired
	private Userrepo userrepo;
	
	@GetMapping("/search/{Query}")
	public ResponseEntity<?>search(@PathVariable("Query") String Queary,Principal principal){
		System.out.println(Queary);
		User user=this.userrepo.getUserByUserName(principal.getName());
		List<Contact>contact=this.contactrepo.findByNameContainingAndUser(Queary, user);
	    return ResponseEntity.ok(contact);
		
		
		
		
		
	}


}
