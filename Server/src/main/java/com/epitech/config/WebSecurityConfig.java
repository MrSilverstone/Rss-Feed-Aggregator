package com.epitech.config;

import com.epitech.filter.JwtTokenAuthenticationFilter;
import com.epitech.filter.RestAccessDeniedHandler;
import com.epitech.filter.SecurityAuthenticationEntryPoint;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.nio.charset.Charset;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Created by loulo on 13/01/2017.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    public WebSecurityConfig() {
        super(true);
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        /// Mettre dans un fichier!!
        String secret = "ksfdlkvopiurutueijflkdsvf,cjnjnxjnvsoifoiropiezaropioezkvf,k,c kv,ckvdkfjgvorieoigfopziefpozepfiezikfozkfldsmvflkvdldvl,fdvk,fdkv,dkfgkjfdgkjvicooiviuzieopiztpoirpotimldkflg,vlkfckjshayauiueruergregierpogipdfogiklxvcjnxvjnfvjdsmkfmslfklgkfgoirgjitrooritjg";

        httpSecurity
                .addFilterAfter(jwtTokenAuthenticationFilter("/**", secret), ExceptionTranslationFilter.class)
                .addFilterAfter(corsFilter(), ExceptionTranslationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
                .accessDeniedHandler(new RestAccessDeniedHandler())
                .and()
                .anonymous()
                .and()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated();
    }

    private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter(String path, String secret) {
        return new JwtTokenAuthenticationFilter(path, secret);
    }

    private CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader(ORIGIN);
        config.addAllowedHeader(CONTENT_TYPE);
        config.addAllowedHeader(ACCEPT);
        config.addAllowedHeader(AUTHORIZATION);
        config.addAllowedMethod(GET);
        config.addAllowedMethod(PUT);
        config.addAllowedMethod(POST);
        config.addAllowedMethod(OPTIONS);
        config.addAllowedMethod(DELETE);
        config.addAllowedMethod(PATCH);
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }
}