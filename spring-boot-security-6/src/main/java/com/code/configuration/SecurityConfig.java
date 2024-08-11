package com.code.configuration;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;

//Enable h2 database
//Database Authentication

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	DataSource dataSource;
	/*because we added h2 database dependency and properties therefore spring 
	 * boot automatically gives this bean to us.there is no need to define this.
	 */
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/h2-console/**").permitAll()
				.anyRequest().authenticated());
		http.sessionManagement(session
				-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		//http.formLogin();
		http.httpBasic();
		http.headers(headers -> 
		headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
		http.csrf(csrf -> csrf.disable());
		return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		
		UserDetails user1 = User.withUsername("user1")
				.password(passwordEncoder().encode("password1"))
				.roles("USER")
				.build();
		
		UserDetails admin = User.withUsername("admin")
				.password(passwordEncoder().encode("adminpass"))
				.roles("ADMIN")
				.build();
		
		JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
		userDetailsManager.createUser(user1);
		userDetailsManager.createUser(admin);
		return userDetailsManager;
		
		//return new InMemoryUserDetailsManager(user1,admin);
	}
	
  /*	InMemoryUserDetailsManager and JdbcUserDetailsManager are implementations of
   *  UserDetailsManager which extends UserDetailsService.InMemoryUserDetailsManager is
   *  used to create users in memory and JdbcUserDetailsManager is used to create users
   *  in database.JdbcUserDetailsManager takes DataSource as an argument.
   */
	
	//password encoding
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// BCryptPasswordEncoder is implementation of PasswordEncoder
	
}
