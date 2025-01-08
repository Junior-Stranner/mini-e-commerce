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

    public String generateToken(UserDetails userDetails) {
        // Cria um token JWT para o usuário.
        // As informações do usuário (como username) são usadas como base para o token.
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        // Cria um token JWT configurando as reivindicações, o assunto, as datas e a assinatura.
        return Jwts.builder()
                .setClaims(claims) // Reivindicações adicionais (vazio por enquanto).
                .setSubject(subject) // Define o assunto do token, geralmente o username.
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data de criação do token.
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Data de expiração.
                .signWith(SignatureAlgorithm.HS256, secret) // Assina o token com o algoritmo HS256 e a chave secreta.
                .compact(); // Compacta e retorna o token como uma string.
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        // Valida o token verificando se o username é o mesmo e se o token não expirou.
        final String username = extractUsername(token); // Extrai o username do token.
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Verifica validade.
    }

    public String extractUsername(String token) {
        // Extrai o nome de usuário do token (armazenado como o "subject").
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // Extrai uma reivindicação específica do token usando um resolutor.
        final Claims claims = extractAllClaims(token); // Obtém todas as reivindicações do token.
        return claimsResolver.apply(claims); // Aplica o resolutor para extrair o valor desejado.
    }

    private Claims extractAllClaims(String token) {
        // Analisa o token e obtém todas as informações contidas nele.
        return Jwts.parser()
                .setSigningKey(secret) // Configura a chave secreta usada para assinar o token.
                .parseClaimsJws(token) // Analisa e valida o token.
                .getBody(); // Retorna o corpo do token, que contém as reivindicações.
    }

    private Boolean isTokenExpired(String token) {
        // Verifica se o token já expirou.
        return extractExpiration(token).before(new Date()); // Compara a data de expiração com a data atual.
    }

    private Date extractExpiration(String token) {
        // Extrai a data de expiração do token.
        return extractClaim(token, Claims::getExpiration);
    }

}