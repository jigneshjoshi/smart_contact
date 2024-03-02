package com.smart_contacts.deo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart_contacts.ENTITIES.Contact;

public interface Contactrepo extends JpaRepository<Contact, Integer>{
	
	@Query("from Contact as c where c.user.id =:userId")
	//curnt page 
	// per page
	public Page<Contact> findContactsbyUser	(@Param("userId") int userId,Pageable pageable);	
	

}
