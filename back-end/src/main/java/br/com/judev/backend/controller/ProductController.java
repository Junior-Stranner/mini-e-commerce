package br.com.judev.backend.controller;

import br.com.judev.backend.dto.ProductDTO;
import br.com.judev.backend.dto.ProductListDTO;
import br.com.judev.backend.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(@RequestPart("product") @Valid ProductDTO productDTO,
                                                    @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        return ResponseEntity.ok(productService.createProduct(productDTO, image));
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
                                                    @RequestPart("product") @Valid ProductDTO productDTO,
                                                    @RequestPart(value = "image", required = false) MultipartFile image)throws IOException{
        return ResponseEntity.ok(productService.updateProduct(id, productDTO, image));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductListDTO>> getAllProducts(@PageableDefault(size=10) Pageable pageable){
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

}

/*
O uso de @RequestPart nos métodos createProduct e updateProduct é necessário para
 lidar com requisições multipart/form-data. Vamos entender cada parte:

@RequestPart
O @RequestPart é usado para extrair partes específicas de uma requisição multipart (como JSON ou arquivos).
Permite mapear partes do corpo da requisição para diferentes parâmetros do método, como objetos ou arquivos.


@RequestPart(value = "image", required = false)
Especifica que a parte da requisição identificada por "image" será mapeada para o parâmetro image.
O tipo do parâmetro é MultipartFile, usado para manipular arquivos enviados via requisição.
O atributo required = false indica que o envio do arquivo não é obrigatório. Ou seja,
 a requisição pode ser enviada sem incluir a parte chamada "image".


Como esses métodos funcionam
createProduct:

Recebe o DTO ProductDTO (extraído da parte "product") e um arquivo opcional (extraído da parte "image").
Passa essas informações para o serviço productService.createProduct.
updateProduct:

Recebe o ID do produto (via URL), o DTO ProductDTO (extraído da parte "product"), e um arquivo opcional (extraído da parte "image").
Passa essas informações para o serviço productService.updateProduct. */
