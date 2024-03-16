package com.smart_contacts.deo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart_contacts.ENTITIES.Otp;

public interface Otprepo extends JpaRepository<Otp, Long> {
	 Optional<Otp> findByOtpValue(Integer fill_otp);
	
}
