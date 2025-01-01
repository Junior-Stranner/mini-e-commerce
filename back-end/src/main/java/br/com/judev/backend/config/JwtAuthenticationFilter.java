package br.com.judev.backend.config;

import br.com.judev.backend.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       final String authorizationHeader = request.getHeader("Authorization");

       String username = null;
       String  jwt = null;

       if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
           jwt = authorizationHeader.substring(7);
           username = jwtService.extractUsername(jwt);
       }
    }
}
