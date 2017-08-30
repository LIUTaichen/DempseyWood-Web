package com.dempseywood;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DWUserDetailService implements UserDetailsService {
	
	private Logger log = LoggerFactory.getLogger(DWUserDetailService.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("calling userservice");
		
		UserDetails user = new User(username, "", null);
		
		// TODO Auto-generated method stub
		return user;
	}
	
		

}
