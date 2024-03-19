package com.smart_contacts.deo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart_contacts.ENTITIES.Todo;

public interface Todotask extends JpaRepository<Todo, Long>{
	

}
