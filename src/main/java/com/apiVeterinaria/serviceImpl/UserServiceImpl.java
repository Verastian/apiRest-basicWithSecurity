package com.apiVeterinaria.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apiVeterinaria.configuration.JwtTokenProvider;
import com.apiVeterinaria.exception.RestServiceException;
import com.apiVeterinaria.model.User;
import com.apiVeterinaria.model.repository.UserRepository;
import com.apiVeterinaria.service.UserService;



@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	
//	@Autowired
	private UserRepository userRepository;

	// Inyeccion de nuevas utilidades de JWT para manejar autenticacion
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void update(User user) {
		// Idem anterior, pero en este caso actualiza con metodo interno de
		// JPARepository
		userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		// Recibe lista de User y luego con metodo map(User::toDTO), convierte c/User en
		// UserDTO
		return userRepository.findAll();
	}

	@Override
	public User findById(Long id) {
		// Retorna un Optional<User> y luego se convierte en un UserDTO
		// Optional es útil para manejar opcionalidades, de esta forma, se evitan
		// valores nulos (NullPointerException desaparecen)
		return userRepository.findById(id).orElse(new User());
	}

	@Override
	public void delete(User user) {
		// Se llama metodo delete y se entrega un User, primero transformandolo de
		// UserDTO a User con metodo toEtity
		userRepository.delete(user);
	}

	@Override
	public String signIn(String username, String password) {
		try {
			// Validar datos de inicio de sesion
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			// Retorna token si los datos son correctos
			return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
		} catch (AuthenticationException e) {
			// Excepcion en caso de datos erroneos
			throw new RestServiceException("username o password invalido", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@Override
	public String signUp(User user) {
		// Valida si el nombre de usuario no exista
		if (!userRepository.existsByUsername(user.getUsername())) {
			// Se encripta contraseña
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			// Se almacena el usuario
			userRepository.save(user);
			// Retrona token valido para este usuario
			return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
		} else {
			// En caso de que nombre de usuario exista se retonra excepcion
			throw new RestServiceException("Username ya esta en uso", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Se busca usuario por su username
		final User user = userRepository.findByUsername(username);
		// Se evalua si usuario existe
		if (user == null) {
			// Si no existe se retorna excepcion de "Usuario no encontrado"
			throw new UsernameNotFoundException("Usuario '" + username + "' no encontrado");
		}
		// Si existe, se retorna un objeto de tipo UserDetails, validando contraseña y
		// su respectivo Rol.
		return org.springframework.security.core.userdetails.User
				.withUsername(username)
				.password(user.getPassword())
				.authorities(user.getRoles())
				.accountExpired(false).accountLocked(false)
				.credentialsExpired(false)
				.disabled(false).build();
	}

}
