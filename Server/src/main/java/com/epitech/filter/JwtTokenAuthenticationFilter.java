package com.epitech.filter;

import com.epitech.utils.JwtUtils;
import com.epitech.exceptions.JwtBadSignatureException;
import com.epitech.exceptions.JwtExpirationException;
import com.epitech.exceptions.MalformedJwtException;
import com.epitech.model.JwtUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import static com.epitech.utils.JwtUtils.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class JwtTokenAuthenticationFilter extends GenericFilterBean {

    private RequestMatcher requestMatcher;
    private String         secretKey;

    public JwtTokenAuthenticationFilter(String path, String secretKey) {
        this.requestMatcher = new AntPathRequestMatcher(path);
        this.secretKey = secretKey;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if(!requiresAuthentication(request)) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            SignedJWT jwt = extractAndDecodeJwt(request);
            checkAuthenticationAndValidity(jwt);
            Authentication auth = buildAuthenticationFromJwt(jwt, request);
            SecurityContextHolder.getContext().setAuthentication(auth);

            chain.doFilter(request, response);

        } catch (JwtExpirationException ex) {
            throw new AccountExpiredException("Token is not valid anymore");
        } catch(JwtBadSignatureException | ParseException | JOSEException ex) {
            throw new MalformedJwtException("Token is malformed");
        }

        /* SecurityContext is then cleared since we are stateless.*/
        SecurityContextHolder.clearContext();
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        return requestMatcher.matches(request);
    }


    private SignedJWT extractAndDecodeJwt(HttpServletRequest request) throws ParseException {
        String authHeader = request.getHeader(AUTHORIZATION);
        String token = authHeader.substring("Bearer ".length());
        return parse(token);
    }

    private void checkAuthenticationAndValidity(SignedJWT jwt) throws ParseException, JOSEException {
        assertNotExpired(jwt);
        assertValidSignature(jwt, secretKey);
    }

    private Authentication buildAuthenticationFromJwt(SignedJWT jwt, HttpServletRequest request) throws ParseException {

        String username = getUsername(jwt);
        Collection<? extends GrantedAuthority> authorities = JwtUtils.getRoles(jwt);
        Date creationDate = getIssueTime(jwt);
        JwtUser userDetails = new JwtUser(username, creationDate, authorities);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }

}
