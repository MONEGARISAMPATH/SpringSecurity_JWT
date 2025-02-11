

package com.SpringSecurity.Configuration;

import java.io.IOException;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.SpringSecurity.Service.JwtService;
import com.SpringSecurity.Service.MyUserDetailsService;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	JwtService jwtService;
	
	@Autowired 
	org.springframework.context.ApplicationContext context;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader =request.getHeader("Authorization");
				String token=null;
		        String username=null;
		        String role = null;
		        
		        if(authHeader !=null && authHeader.startsWith("Bearer ")) {
		        	token=authHeader.substring(7);
		        	username=jwtService.extractUserName(token);
		        }
		        
		        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
		            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
		            role = jwtService.extractRoleFromToken(token);

		            if (role != null && !role.trim().isEmpty()) {
		                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
		                        userDetails, null, Collections.singleton(new SimpleGrantedAuthority(role)));

		                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		                SecurityContextHolder.getContext().setAuthentication(authToken);
		            } else {
		                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Role is missing or invalid in the token.");
		                return; // Stop further processing if the role is missing
		            }
		        }
		    
		    filterChain.doFilter(request, response);
	}
}
