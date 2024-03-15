package com.smart_contacts.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart_contacts.ENTITIES.User;
import com.smart_contacts.deo.Userrepo;
import com.smart_contacts.halper.Message;

@Controller
public class Mainhandler {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
		
	
     @Autowired
	private Userrepo userrepo;
	
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home-digitalcontactbook");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "about-digitalcontactbook");
        return "about";
    }

    @GetMapping("/signin")
    public String login(Model model) {
        model.addAttribute("title", "login-digitalcontactbook");
       return "login";
    }
    
    @GetMapping("/login_fail")
    public String loginfail(Model model) {
    	model.addAttribute("title", "error");
    	model.addAttribute("error_msg", "credentials not match");
        return "login_error";}
    

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "signup-digitalcontactbook");
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/do_register")
    public String register(@Valid@ModelAttribute("user") User user,BindingResult result,@RequestParam(value = "agreed", defaultValue = "false") boolean agreed, Model model ,HttpSession session) {
    	try {
    	    if (!agreed) {
    	        throw new Exception("Please agree to the terms and conditions");
    	    }
    	    if(result.hasErrors()) {
    	    	model.addAttribute("user", user);
    	    	return "signup";
    	    }
    	    user.setRole("ROLE_USER");
    	    user.setEnabled(true);
    	    user.setPassword(passwordEncoder.encode(user.getPassword()));
    	    // Perform registration logic here (save user to database, etc.)
    	     this.userrepo.save(user);
    	    

    	    // Set success message in session
    	    session.setAttribute("message", new Message("Successfully registered!", "alert-success"));

    	    model.addAttribute("user", new User());
    	    return "signup";
    	} catch (Exception e) {
    	    e.printStackTrace();
    	    model.addAttribute("user", user);

    	    // Set error message in model
    	    session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));

    	   

    	    return "signup";
    	}

    	}
      
  
    
    }

