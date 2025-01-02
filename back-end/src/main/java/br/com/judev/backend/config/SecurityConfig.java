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
/*Habilita o Spring Security na aplicação.
Permite configurar a segurança da aplicação
usando classes que estendem SecurityConfigurerAdapter ou configurando um bean do tipo SecurityFilterChain.*/
@EnableWebSecurity

/*
Habilita a segurança baseada em anotações nos métodos da aplicação.
Permite o uso de anotações como @PreAuthorize, @PostAuthorize, @Secured,
e @RolesAllowed para definir regras de segurança no nível do método.*/
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public SecurityConfig(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

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


/*@EnableWebSecurity
Por que é necessário: O Spring Security, por padrão, aplica uma configuração básica de
segurança, mas para personalizar o comportamento (como autenticação, autorização, ou filtros personalizados),
é necessário habilitar explicitamente a segurança da web.
 */

/*@EnableMethodSecurity
Por que é necessário: Às vezes, é necessário aplicar segurança não apenas no
nível dos endpoints, mas também em métodos de serviços ou outras classes internas.
Essa anotação permite fazer isso.*/

/*
@PreAuthorize: Avalia uma expressão de segurança antes de o método ser executado.
@PostAuthorize: Avalia uma expressão de segurança após o método ser executado.
@Secured: Especifica os papéis que podem acessar o método.
@RolesAllowed: Similar a @Secured, mas baseada em JSR-250.
 */