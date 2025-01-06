package br.com.judev.backend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    // Método para gerar um token JWT baseado nos detalhes do usuário.
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(); // Map para armazenar informações adicionais no token (neste caso, vazio).
        return createToken(claims, userDetails.getPassword()); // Cria o token usando as informações fornecidas.
    }

    // Método privado para criar o token JWT com as informações e o tempo de expiração.
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) // Define as informações adicionais no token.
                .setSubject(subject) // Define o "subject" do token (geralmente o identificador do usuário).
                .setIssuedAt(new Date(System.currentTimeMillis())) // Define a data de emissão do token.
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Define a data de expiração do token.
                .signWith(SignatureAlgorithm.HS256, secret) // Assina o token com o algoritmo HS256 e o secret.
                .compact(); // Compacta o token em uma string final.
    }

    // Valida o token verificando o username e se o token ainda não expirou.
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Extrai o username do token.
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Verifica se o username bate e se o token não expirou.
    }

    // Extrai o username (subject) do token.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Usa o método genérico extractClaim para pegar o "subject".
    }

    // Método genérico para extrair qualquer informação do token usando um resolver.
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // Recupera todas as claims (informações) do token.
        return claimsResolver.apply(claims); // Aplica a função resolver para extrair a informação desejada.
    }

    // Extrai todas as claims (informações) do token JWT.
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret) // Define o secret usado para assinar o token.
                .parseClaimsJws(token) // Analisa o token e valida a assinatura.
                .getBody(); // Retorna o corpo do token (claims).
    }

    // Verifica se o token está expirado.
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Compara a data de expiração com a data atual.
    }

    // Extrai a data de expiração do token.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Usa o método genérico extractClaim para pegar a data de expiração.
    }
}