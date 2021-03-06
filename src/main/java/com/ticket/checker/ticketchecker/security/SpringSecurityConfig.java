package com.ticket.checker.ticketchecker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ticket.checker.ticketchecker.TicketCheckerApplication;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	public static final String ADMIN = "ADMIN";
	public static final String PUBLISHER = "PUBLISHER";
	public static final String VALIDATOR = "VALIDATOR";
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private AppAuthenticationEntryPoint authenticationEntryPoint;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers("/images/**").permitAll()
			.antMatchers("/users/**").hasRole(ADMIN)
			.antMatchers(HttpMethod.DELETE,"/tickets/**").hasRole(ADMIN)
			.antMatchers(HttpMethod.POST, "/tickets").hasAnyRole(ADMIN, PUBLISHER)
			.antMatchers(HttpMethod.POST, "/tickets/validate/**").hasAnyRole(ADMIN, VALIDATOR)
			.antMatchers(HttpMethod.POST, "/tickets/**").hasRole(ADMIN)
			.anyRequest().authenticated();
		http.httpBasic().realmName(TicketCheckerApplication.REALM_NAME).authenticationEntryPoint(authenticationEntryPoint);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}
	
	@Bean
	public static PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder(11);
	}

}
