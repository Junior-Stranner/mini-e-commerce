package br.com.judev.backend.config;

import br.com.judev.backend.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Obtém o cabeçalho "Authorization" da requisição.
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null; // Armazena o nome do usuário extraído do token.
        String jwt = null; // Armazena o token JWT extraído do cabeçalho.

        // Verifica se o cabeçalho existe e começa com "Bearer" (indicando um token JWT válido).
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            jwt = authorizationHeader.substring(7); // Remove o prefixo "Bearer " do token.
            username = jwtService.extractUsername(jwt); // Extrai o nome de usuário do token usando o JwtService.
        }

        // Verifica se um nome de usuário foi extraído e se o contexto de segurança ainda não contém autenticação.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carrega os detalhes do usuário a partir do UserDetailsService.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Valida o token JWT e verifica se ele pertence ao usuário correspondente.
            if (jwtService.validateToken(jwt, userDetails)) {
                // Cria um token de autenticação baseado nos detalhes do usuário.
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Define os detalhes adicionais da autenticação, como a origem da requisição.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Configura o contexto de segurança com o token de autenticação.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continua o fluxo da requisição, passando para o próximo filtro na cadeia.
        filterChain.doFilter(request, response);
    }
}
