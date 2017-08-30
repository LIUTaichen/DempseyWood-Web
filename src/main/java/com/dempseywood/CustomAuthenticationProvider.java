package com.dempseywood;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	private Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		// TODO Auto-generated method stub
		return new UsernamePasswordAuthenticationToken(name, "", new ArrayList<>());
	}

	@Override
	public boolean supports(Class<?> authentication) {

		// TODO Auto-generated method stub
		return authentication.equals(
		          UsernamePasswordAuthenticationToken.class);
	}

}
