package com.authen.config;

import com.authen.repository.UsersRepository;
import com.authen.security.*;
import com.authen.security.login.JwtAuthenticationLoginProvider;
import com.authen.security.match.MatchLoginRegister;
import com.authen.security.match.SkipPathRequestMatcher;
import com.authen.security.register.JwtRegisterProvider;
import com.authen.services.impl.CustomUserDetailsServiceImpl;
import com.authen.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint entryPoint;

    @Autowired
    private JwtAuthenticationLoginRegisterSuccessHandler jwtAuthenticationLoginRegisterSuccessHandler;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private JwtRegisterProvider jwtRegisterProvider;

    @Autowired
    private JwtAuthenticationLoginProvider jwtAuthenticationLoginProvider;


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


//    @Bean
//    public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
//        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
//        authenticationTokenFilter.setAuthenticationManager(super.authenticationManagerBean());
//        return authenticationTokenFilter;
//    }

    @Bean
    public UserDetailsService userDetailsService(UsersRepository usersRepository) {
        return new CustomUserDetailsServiceImpl(usersRepository);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(
//                new BCryptPasswordEncoder());
        auth.authenticationProvider(jwtAuthenticationProvider);
        auth.authenticationProvider(jwtRegisterProvider);
        auth.authenticationProvider(jwtAuthenticationLoginProvider);
    }

    protected JwtLoginRegisterProcessingFilter jwtLoginRegisterProcessingFilter() {
        List<String> matchs = new ArrayList<>();
        matchs.add(Constants.ENPOINT_LOGIN);
        matchs.add(Constants.ENPOINT_REGISTER);
        MatchLoginRegister matchLoginRegister = new MatchLoginRegister(matchs);
        JwtLoginRegisterProcessingFilter filter =
                new JwtLoginRegisterProcessingFilter(matchLoginRegister,
                        jwtAuthenticationLoginRegisterSuccessHandler,
                        new JwtAuthenticationLoginRegisterFailureHandler(objectMapper),
                        objectMapper, userDetailsService);
        try {
            filter.setAuthenticationManager(authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filter;
    }


    protected JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        List<String> listNotmap = new ArrayList<>();
        listNotmap.add(Constants.ENPOINT_LOGIN);
        listNotmap.add(Constants.ENPOINT_REGISTER);
        listNotmap.add(Constants.ENPOINT_MATCH_API);
        RequestMatcher matcher = new SkipPathRequestMatcher(listNotmap, Constants.ENPOINT_MATCH_AUTH_API);
        JwtAuthenticationTokenFilter filter =
                new JwtAuthenticationTokenFilter(matcher);
        try {
            filter.setAuthenticationManager(authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // filter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
        return filter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
//        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization", "TokenType"));
//        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
//        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization", "TokenType"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//
//        return new CorsFilter(source);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // http.cors().and();
        // http.cors().and();
        http.csrf().disable().cors()
                .configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()).and()
                .exceptionHandling()
                .authenticationEntryPoint(entryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(Constants.ENPOINT_LOGIN, Constants.ENPOINT_LOGIN).permitAll() // Login end-point
//                .and().authorizeRequests()
                .antMatchers(Constants.ENPOINT_MATCH_AUTH_API).authenticated()
//                .and().authorizeRequests()
                .antMatchers(Constants.ENPOINT_MATCH_API).permitAll()
                .antMatchers(Constants.ENPOINT_MATCH_AUTH_API_ACTIVE_USER).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtLoginRegisterProcessingFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class);

    }

}
