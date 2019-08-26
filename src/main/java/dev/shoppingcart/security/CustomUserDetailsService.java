package dev.shoppingcart.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.shoppingcart.domain.User;
import dev.shoppingcart.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository
					.findOneByEmailIgnoreCase(username)
					.orElseThrow(() -> new UsernameNotFoundException("User with Email: " + username + ", not found!!!"));
		return new org.springframework.security.core.userdetails.User(user.getEmail()
				, user.getPassword()
				, AuthorityUtils.createAuthorityList(user.getRole()));
	}

}
