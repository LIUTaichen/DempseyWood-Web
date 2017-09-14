package com.dempseywood.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DWUserDetailService userDetailServce;
	@Autowired
	private CustomAuthenticationProvider authenticationProvider;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
	        .sessionManagement()
		        .invalidSessionUrl("/invalidSession")
		        .and()
        	.csrf().disable()
            .authorizeRequests()
                .antMatchers( "/css/**", "/webjars/**", "/images/**", "/js/**", "/invalidSession").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(userDetailServce);
    	 auth.authenticationProvider(authenticationProvider);
        
          /*  .inMemoryAuthentication()
                .withUser("jason.liu@dempseywood.co.nz").password("password").roles("USER");*/
        
    }

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
				.ignoring()
				.antMatchers("/api/**");
	}
}