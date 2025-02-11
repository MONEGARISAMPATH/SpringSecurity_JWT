package com.SpringSecurity.Entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class StudentPrincipal implements UserDetails {

	private Student student;

	public  StudentPrincipal(Student student) {
		this.student = student;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return Collections.singleton(new SimpleGrantedAuthority(student.getRole()));
	}

	@Override
	public String getPassword() {

		return student.getPassword();
	}

	@Override
	public String getUsername() {

		return student.getName();
	}

}
