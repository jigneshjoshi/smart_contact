package com.smart_contacts.deo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart_contacts.ENTITIES.User;

public interface Userrepo extends JpaRepository<User,Integer>{
	@Query("select u from User u where u.email =:email")
	public User getUserByUserName(@Param("email") String email);
	
	
	
	

}
