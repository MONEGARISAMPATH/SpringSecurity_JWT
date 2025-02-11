package com.SpringSecurity.Service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.SpringSecurity.Entity.Student;
import com.SpringSecurity.Repository.StudentRepository;

@Service
public class StudentService {

	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	AuthenticationManager authManger;
	
	@Autowired
	JwtService jwtservice;
	
	private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);
	
	public Student createStudent(Student student) {
		student.setPassword(encoder.encode(student.getPassword()));
		if (student.getRole() == null) {
	        student.setRole("STUDENT"); // Default Role
	    }
		
		return studentRepository.save(student); //password :Sampath@12, name: Sampath
	}
	
 public Student findById(Integer id) {
	 return studentRepository.findById(id).get();
 }

	
	public List<Student> getAllStudents(){ 
		return studentRepository.findAll();
	}

	public String verify(Student student) {
             Authentication authentication=authManger.authenticate(new UsernamePasswordAuthenticationToken(student.getName(), student.getPassword()));
             
            if( authentication.isAuthenticated()) {
            	Student loggedInStudent = studentRepository.findStudentByName(student.getName());
                return jwtservice.generateToken(loggedInStudent);
            }
             
             return "fail";
	}
	
}
