package com.SpringSecurity.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {
		
	//	http.csrf(customizer -> customizer.disable()); //this for crsf disable
	//	http.authorizeHttpRequests(request -> request.anyRequest().authenticated()); //any request from end point accessed get ,post etc
	//	http.formLogin(Customizer.withDefaults()); // from login for chrome 
   //		http.httpBasic(Customizer.withDefaults()); //api tools like postman for sign up page
	//	http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //for every request session generates new one
  //		return http.build();
		
		
		
	 return http.csrf(customizer -> customizer.disable())
				.authorizeHttpRequests(request -> request
						.requestMatchers("/student/register","/student/login","/actuator/**").permitAll()//Except this endpoint has authentication
						.requestMatchers("/student/admin/**").hasAuthority("ADMIN")
						.requestMatchers("/student/allstudents").hasAuthority("STUDENT")
						.anyRequest().authenticated())
				//.formLogin(Customizer.withDefaults())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class)
				.build();		
	}
	
	 @Bean
	 AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		
		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}
	 @Bean
	 AuthenticationManager authenticationManager(AuthenticationConfiguration config ) throws Exception{
		 return config.getAuthenticationManager();
		 
	 }
	 
	 
	 
}
