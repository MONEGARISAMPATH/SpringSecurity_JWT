package com.SpringSecurity.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.SpringSecurity.Entity.Student;
import com.SpringSecurity.Entity.StudentPrincipal;
import com.SpringSecurity.Repository.StudentRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	


	@Autowired
	StudentRepository studentRepository;

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

		Student student=studentRepository.findStudentByName(name);
		
		if(student==null) {
			System.out.println("Student not found ");
			throw new UsernameNotFoundException("not found Student is:  "+name);
			
		}
		return new StudentPrincipal(student);
	}

}
