package dev.shoppingcart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.shoppingcart.security.jwt.JWTConfigurer;
import dev.shoppingcart.security.jwt.JWTTokenProvider;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
    @Autowired
    JWTTokenProvider jwtTokenProvider;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers("/auth/signin").permitAll()
                .antMatchers("/auth/signup").permitAll()
                .antMatchers("/v2/api-docs", "/swagger-resources/configuration/ui", "/swagger-resources", "/swagger-resources/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("CUSTOMER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/product-orders/**").hasAnyRole("CUSTOMER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/product-orders/**").hasRole("CUSTOMER")
                .antMatchers(HttpMethod.PUT, "/api/product-orders/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/product-orders/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
            .apply(new JWTConfigurer(jwtTokenProvider));
    	http.headers().frameOptions().disable();
    }
}
