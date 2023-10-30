package com.example.solution.security;

import com.example.solution.security.services.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private PasswordEncoder passwordEncoder;
    private UserDetailServiceImpl userDetailServiceImpl;
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder.encode("1234")).roles ("USER").build(),
                User.withUsername("user2").password(passwordEncoder.encode("1234")).roles ("USER").build(),
                User.withUsername("admin").password(passwordEncoder.encode("1234")).roles ("ADMIN").build()
        );
    }
  /*  @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                ;
        http.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
              .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }*/
        @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login").defaultSuccessUrl("/").permitAll();
    /*    http.authorizeRequests().antMatchers("/register").permitAll().antMatchers("/welcome")
                .hasAnyRole("USER", "ADMIN").antMatchers("/getEmployees").hasAnyRole("USER", "ADMIN")
                .antMatchers("/addNewEmployee").hasAnyRole("ADMIN").anyRequest().authenticated().and().formLogin()
                .loginPage("/login").permitAll().and().logout().permitAll();
        http.csrf().disable();*/
      //// http.authorizeRequests().requestMatchers("/user/**").hasRole("USER");
    //   //http.authorizeRequests().requestMatchers("/admin/**").hasRole("ADMIN");
        http.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable);
        //http.rememberMe();
        http.exceptionHandling().accessDeniedPage("/notAuthorized");
        http.userDetailsService(userDetailServiceImpl);

        return http.build();
    }
}

