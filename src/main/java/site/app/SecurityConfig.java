package site.app;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import site.model.User;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Resource
	public ApplicationContext context;

	@Override
	public void configure(final WebSecurity web) throws Exception {
		web
			.ignoring()
				.antMatchers("/asset/**")
				.antMatchers("/css/**")
				.antMatchers("/fonts/**")
				.antMatchers("/images/**")
				.antMatchers("/js/**");
	}
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
                .and()
                .withUser("admin").password("password").roles("ADMIN","USER");
//	        auth.authenticationProvider(new AuthenticationProvider() {
//	            @Override
//	            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//	                String email = (String) authentication.getPrincipal();
//	                String providedPassword = (String) authentication.getCredentials();
//	                User user = userService.findAndAuthenticateUser(email, providedPassword);
//	                if (user == null) {
//	                    throw new BadCredentialsException("Username/Password does not match for " + authentication.getPrincipal());
//	                }
//
//	                return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//	            }
//
//	            @Override
//	            public boolean supports(Class<?> authentication) {
//	                return true;
//	            }
//	        });
    }

	 @Override
     protected void configure(final HttpSecurity http) throws Exception {
		 http
         .authorizeRequests()
           .antMatchers("/","/login","/about", "/nav/**").permitAll() // #4
           .antMatchers("/admin/**").hasRole("ADMIN") // #6
           .anyRequest().authenticated() // 7
           .and()
       .formLogin()  // #8
           .loginPage("/login") // #9
           .permitAll(); // #5
	 }
	 
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
