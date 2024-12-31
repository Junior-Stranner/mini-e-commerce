package br.com.judev.backend.config;

import br.com.judev.backend.repositories.UserRepository;
import br.com.judev.backend.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configura o objeto HttpSecurity para gerenciar as regras de segurança da aplicação.
        http
                // Desabilita o CSRF (Cross-Site Request Forgery), pois em APIs REST geralmente não é necessário
                // devido ao uso de tokens para autenticação.
                .csrf(csrf -> csrf.disable())

                // Configura as regras de autorização para diferentes endpoints.
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso público à raiz do site ("/").
                        .requestMatchers("/").permitAll()

                        // Permite acesso público a arquivos estáticos na pasta "/images".
                        .requestMatchers("/images/**").permitAll()

                        // Permite acesso público ao arquivo "/index.html".
                        .requestMatchers("/index.html").permitAll()

                        // Permite acesso público a todos os endpoints que começam com "/api/auth".
                        .requestMatchers("/api/auth/**").permitAll()

                        // Permite acesso público apenas para requisições GET no endpoint "/api/products/**".
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                        // Restringe o endpoint "/api/auth/change-password" para usuários autenticados.
                        .requestMatchers("/api/auth/change-password").authenticated()

                        // Qualquer outra requisição deve ser autenticada.
                        .anyRequest().authenticated()
                )

                // Configura o gerenciamento de sessões.
                .sessionManagement(session -> session
                        // Define a política de criação de sessões como STATELESS.
                        // Isso significa que não serão mantidas sessões no servidor,
                        // uma prática comum em APIs REST que utilizam tokens JWT.
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Adiciona o filtro de autenticação JWT antes do filtro padrão de autenticação
                // por nome de usuário e senha (UsernamePasswordAuthenticationFilter).
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // Constrói e retorna o objeto SecurityFilterChain configurado.
        return http.build();
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtService, userDetailsService());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> (UserDetails) userRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // Cria uma instância de DaoAuthenticationProvider, que é um provedor de autenticação
        // baseado em uma fonte de dados DAO (Data Access Object).
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // Configura o serviço responsável por carregar os detalhes do usuário (UserDetailsService).
        // Esse serviço fornece as informações do usuário (como nome de usuário e senha) para validação.
        authProvider.setUserDetailsService(userDetailsService());

        // Define o codificador de senhas (PasswordEncoder) que será usado para verificar
        // se a senha fornecida corresponde à senha armazenada.
        // Isso garante que as senhas sejam verificadas de forma segura.
        authProvider.setPasswordEncoder(passwordEncoder());

        // Retorna o provedor de autenticação configurado.
        return authProvider;
    }

}
