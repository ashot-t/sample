package dev.shoppingcart.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.shoppingcart.domain.User;
import dev.shoppingcart.repository.UserRepository;
import dev.shoppingcart.web.rest.RegisterRequest;
import dev.shoppingcart.web.rest.errors.BadRequestException;

@Service
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User registerUser(RegisterRequest req) {
        userRepository.findOneByEmailIgnoreCase(req.getEmail()).ifPresent(existingUser -> {
                throw new BadRequestException("Email: " + existingUser.getEmail() + ", is already in use !!!");
        });
        User newUser = new User();
        newUser.setEmail(req.getEmail().toLowerCase());
        newUser.setPassword(passwordEncoder.encode(req.getPassword()));
        newUser.setFirstName(req.getFirstName());
        newUser.setLastName(req.getLastName());
        newUser.setRole("ROLE_CUSTOMER");
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }
	
}
