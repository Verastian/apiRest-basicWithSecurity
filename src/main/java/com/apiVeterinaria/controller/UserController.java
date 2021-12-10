package com.apiVeterinaria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apiVeterinaria.model.User;
import com.apiVeterinaria.service.UserService;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
//	@Autowired
	private UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<User> findAll() {
		return userService.findAll();
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public User findOne(@PathVariable Long id) {
		//@PathVariable: Recibe id desde la URL
		return userService.findById(id);
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public void update(@RequestBody User user) {
		userService.update(user);
	}
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public void delete(@RequestBody User user) {
		userService.delete(user);
	}
	
	@PostMapping("/signin")
	public String login(@RequestParam String username, @RequestParam String password) {
		return userService.signIn(username, password);
	}
	
	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public String signup(@RequestBody User user) {
		return userService.signUp(user);
	}
}
