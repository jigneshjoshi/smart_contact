package com.smart_contacts.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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

import com.smart_contacts.ENTITIES.Otp;
import com.smart_contacts.ENTITIES.User;
import com.smart_contacts.deo.Otprepo;
import com.smart_contacts.deo.Userrepo;
import com.smart_contacts.halper.Generate_method;
import com.smart_contacts.halper.Message;
import com.smart_contacts.service.Emailservice;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class Mainhandler {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
		@Autowired
		private Generate_method generate_method;
	
     @Autowired
	private Userrepo userrepo;
     @Autowired
     private Emailservice emailservice;
     @Autowired
     private Otprepo otprepo;
	
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
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("title", "login-digitalcontactbook");
        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }
       return "login";
    }
    
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
    
    
    
    
    @GetMapping("/forgotpage")
    public String forgot_pass() {
    	
    	return "forgotpage";
			
		}
    
    @PostMapping("/sendotp")
    public String sendotp(@RequestParam("email") String email, HttpSession httpSession) {
    	
    	try {
    		int value = generate_method.generateOTP();
            Otp otp =new Otp();
        	otp.setOtpValue(value);
        	otp.setEmail(email);
        	httpSession.setAttribute("email", email);
        	otp.setCreatedAt(LocalDateTime.now());    	
        	otprepo.save(otp);
        	String  subject = "Reset your password";
        	String message= "otp for reset your password"+value;
        	emailservice.sendemail(subject, email, message);
            System.out.println("Generated OTP: " + otp);
        	return "verify_page";
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "error_page";
    	
    	
    	
    }
 
    }
    
   @PostMapping("/verify-otp")
   public String verifyedotp(@RequestParam("fill_otp") Integer fill_otp,Model model){
	   // Retrieve OTP from the database
       Optional<Otp> otpOptional = otprepo.findByOtpValue(fill_otp);
       
       if (otpOptional.isPresent()) {
           Otp otp = otpOptional.get();
           LocalDateTime otpCreationTime = otp.getCreatedAt();
           long minutesElapsed = ChronoUnit.MINUTES.between(otpCreationTime, LocalDateTime.now());
           
           // Check if OTP has expired (e.g., expired after 1 minutes)
           if (minutesElapsed <= 1) {
               // OTP is still valid
               return "newPassword";
           } else {
               // OTP has expired
              model.addAttribute("message", "OTP has expired. Please request a new one.");
             //  model.addAttribute("messageClass", "error");
               return "verify_page";
           }
       } else {
           // OTP not found in the database
           model.addAttribute("message", "Invalid OTP. Please try again.");
           //model.addAttribute("messageClass", "error");
           return "verify_page";
       }
   
	   
   }
   @PostMapping("/newpass")
   public String newPass(@RequestParam("password")String password,HttpSession httpSession) {
	   String email= (String)httpSession.getAttribute("email");
	   User user =this.userrepo.getUserByUserName(email);
	    if (user == null) {
	        // Handle case when user not found
	        return "redirect:/forgot_pass"; // Redirect back to the forgot password page
	    }
	    
	    // Update user's password
	    user.setPassword(this.passwordEncoder.encode(password));
	    this.userrepo.save(user);
	    
	    httpSession.removeAttribute("email");
	   
	    
	    return "login"; // Redirect to the sign-in page
	
   }
   
   }
  
  
    

