package com.apiVeterinaria.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//import cl.edutecno.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String username;
	private String email;
	private String password;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<Role> roles;
	
	//convierte la entidad User a un UserDTO
//	public UserDTO toDTO(){
//		
//		UserDTO userDTO = new UserDTO();
//		userDTO.setId(this.getId());
//		userDTO.setName(this.getName());
//		userDTO.setUsername(this.getUsername());
//		userDTO.setEmail(this.getEmail());
//		userDTO.setPassword(this.getPassword());
//		userDTO.setRoles(this.getRoles());
//		
//		return userDTO;
//	}

}
