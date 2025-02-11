package com.SpringSecurity.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.SpringSecurity.Entity.Student;
import com.SpringSecurity.Service.StudentService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	StudentService studentService;
	
	@GetMapping("/request")

    public String role(HttpServletRequest request) {
		return "Hey This Is Sampath,Java Developer"  + "   " + request.getSession().getId();
	}
	
	@GetMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(value=HttpStatus.OK)
	public Student findById(@PathVariable Integer id) {
		return studentService.findById(id);
	}
	
	@GetMapping("/allstudents")
	@PreAuthorize("hasRole('STUDENT')")
	@ResponseStatus(value=HttpStatus.OK)
		public List<Student> allStudents(){
			return studentService.getAllStudents();
		}
		
	@PostMapping("/register")
	@ResponseStatus(value=HttpStatus.CREATED)
	public Student createStudent(@RequestBody Student student) {
		return studentService.createStudent(student);
	}
	
	@PostMapping("/login")
	public String  login(@RequestBody Student student) {
		
		return  studentService.verify(student);
	}
	
}
