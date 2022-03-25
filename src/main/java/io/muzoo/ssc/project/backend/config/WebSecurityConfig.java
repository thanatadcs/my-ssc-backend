package io.muzoo.ssc.project.backend.config;

import io.muzoo.ssc.project.backend.OurUserDetailsService;
import io.muzoo.ssc.project.backend.SimpleResponseDTO;
import io.muzoo.ssc.project.backend.util.AjaxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private OurUserDetailsService ourUserDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// configuring security for REST backend APIs because we will not log in using form login anymore.
		// disable csrf, normally you shouldn't but life is a lot easier without it.
		http.csrf().disable();
		// Permit root and /api/login and /api/logout
		http.authorizeRequests()
				.antMatchers("/", "/api/login", "/api/logout", "/api/whoami", "/api/create").permitAll();
		// permit all OPTIONS requests
		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll();

		// Handle error output as JSON for unauthorized access
		http.exceptionHandling()
				.authenticationEntryPoint(new JsonHttp403ForbiddenEntryPoint());

		// Set every other path to require authentication
		http.authorizeRequests().antMatchers("/**").authenticated();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}

	@Override
	public UserDetailsService userDetailsService() {
		return ourUserDetailsService;
	}

	class JsonHttp403ForbiddenEntryPoint implements AuthenticationEntryPoint {

		@Override
		public void commence(HttpServletRequest request,
							 HttpServletResponse response,
							 AuthenticationException authException) throws IOException, ServletException {

			// output JSON message
			String ajaxJson = AjaxUtils.convertToString(
					SimpleResponseDTO
							.builder()
							.success(false)
							.message("Forbidden")
							.build()
			);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			response.getWriter().println(ajaxJson);
		}
	}
}
