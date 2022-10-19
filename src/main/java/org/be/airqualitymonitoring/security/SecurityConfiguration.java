
package org.be.airqualitymonitoring.security;

import org.be.airqualitymonitoring.service.UserService;
import org.be.airqualitymonitoring.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration {
 
	private  UserService userService;
	private BCryptPasswordEncoder passwordEncoder;

	
	 @Autowired
	    public SecurityConfiguration(@Lazy UserService userService,@Lazy BCryptPasswordEncoder passwordEncoder) {
	        this.userService = userService;
	        this.passwordEncoder=passwordEncoder;
	    }
	 

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         
		http.csrf().disable().authorizeRequests()
		.antMatchers(HttpMethod.POST,"/measurements").permitAll()
		.antMatchers("/actuator/**","/breakpointAQI/**","/measurements/**").permitAll()
		.antMatchers("/signup**", "/login","/sensor","/sensors").permitAll()
				.and().formLogin().permitAll()
				.and().logout().invalidateHttpSession(true).clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
				.permitAll();
 
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/resources/static/css/**", "/resources/static/js/**");
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	     return authenticationConfiguration.getAuthenticationManager();
	}
	
}
