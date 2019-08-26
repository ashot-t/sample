package dev.shoppingcart.web.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.shoppingcart.domain.User;
import dev.shoppingcart.repository.UserRepository;
import dev.shoppingcart.security.jwt.JWTTokenProvider;
import dev.shoppingcart.service.UserService;
import dev.shoppingcart.web.rest.errors.BadRequestException;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    JWTTokenProvider jwtTokenProvider;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    UserService userService;
    
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody AuthRequest data) {
        try {
            String email = data.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, data.getPassword()));
            List<String> roles = new ArrayList<>();
            String role = userRepository.findOneByEmailIgnoreCase(email).orElseThrow(() -> new UsernameNotFoundException("User with Email: " + email + ", not found")).getRole();
            roles.add(role);
            String token = jwtTokenProvider.createToken(email, roles);
            Map<Object, Object> model = new HashMap<>();
            model.put("username", email);
            model.put("token", token);
            return ResponseEntity.ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody RegisterRequest data) {
    	try {
    		User newUser = userService.registerUser(data);
    		Map<String, String> result = new HashMap<>();
    		result.put("email", newUser.getEmail());
    		result.put("firstName", newUser.getFirstName());
    		result.put("lastName", newUser.getLastName());
    		return ResponseEntity.ok(result);
    	} catch (BadRequestException bre) {
			throw new BadRequestException(bre.getMessage());
		}
    }
    
}