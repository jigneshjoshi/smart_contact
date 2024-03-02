package com.smart_contacts.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart_contacts.ENTITIES.User;
import com.smart_contacts.deo.Userrepo;

public class CustomUserDetailsImp implements UserDetailsService{

	@Autowired
	private Userrepo userrepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userrepo.getUserByUserName(username);
		if(user==null) {
			throw new UsernameNotFoundException("could not found user!!");
			
		}
		 CustomUserDetails customUserDetails = new CustomUserDetails(user);;
		return customUserDetails;
	}

}
