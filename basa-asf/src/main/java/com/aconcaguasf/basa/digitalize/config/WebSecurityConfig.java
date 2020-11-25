package com.aconcaguasf.basa.digitalize.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/App" ,"/css/**", "/ts/**", "/js/**", "/img/**","/remito/**").permitAll()
				.antMatchers("/home", "/").fullyAuthenticated()
				.antMatchers("/publisher/**").hasAuthority("PUBLISHER").anyRequest().fullyAuthenticated()
				.and()
			.formLogin()
				.loginPage("/login").defaultSuccessUrl("/home").failureUrl("/login?error").usernameParameter("username").permitAll()
				.and()
			.logout()
				.logoutUrl("/logout").logoutSuccessUrl("/login").permitAll()
				.and()
			.exceptionHandling()
				.accessDeniedPage("/403")
				.and()
			.csrf()
				.disable();
	}

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Configuration
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	class SecurityConfig extends WebSecurityConfigurerAdapter {
		// contents as before
	}
	@Bean
	public ShaPasswordEncoder passwordEncoder() {
		return new ShaPasswordEncoder();
	}
}