package br.com.judev.backend;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "br.com.judev")
/*Informa ao Spring Boot onde procurar as classes anotadas com
@Entity (entidades JPA) para que possam ser gerenciadas pelo contexto do JPA.

@EntityScan: Define onde estão localizadas as classes de entidade (@Entity).
*/
@EntityScan(basePackages = "br.com.judev.backend.model")
/*Habilita o Spring Data JPA e configura onde o Spring deve buscar as interfaces de repositórios.
*
* @EnableJpaRepositories: Define onde estão localizados os repositórios JPA (@Repository ou extendendo JpaRepository).
 */
@EnableJpaRepositories(basePackages = "br.com.judev.backend.repositories")
@OpenAPIDefinition(
        info = @Info(
                title = "Back-end Part from e-commerce",
                description = "e-commerce  REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Junior Stranner",
                        email = "tutor@judev.com",
                        url = "https://www.judev.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.judev.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description =  "e-commerce  REST API Documentation",
                url = "https://www.judev.com/swagger-ui.html"
        )
)
public class BackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }

}
