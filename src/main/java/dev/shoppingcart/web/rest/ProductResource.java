package dev.shoppingcart.web.rest;

import dev.shoppingcart.service.ProductService;
import dev.shoppingcart.web.rest.errors.BadRequestException;
import dev.shoppingcart.web.rest.errors.ProductNotFoundException;
import io.swagger.annotations.ApiOperation;
import dev.shoppingcart.service.dto.ProductDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;

/**
 * REST controller for managing {@link cart.shopping.domain.Product}.
 */
@RestController
@RequestMapping("/api")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    /**
     * {@code POST  /products} : Create a new product.
     *
     * @param productDTO the productDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productDTO, or with status {@code 400 (Bad Request)} if the product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) throws URISyntaxException {
        log.debug("REST request to save Product : {}", productDTO);
        if (productDTO.getId() != null) {
            throw new BadRequestException("A new product cannot already have an ID");
        }
        ProductDTO result = productService.save(productDTO);
        return ResponseEntity
        		.created(new URI("/api/products/" + result.getId()))
        		.body(result);
    }

    /**
     * {@code PUT  /products} : Updates an existing product.
     *
     * @param productDTO the productDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productDTO,
     * or with status {@code 400 (Bad Request)} if the productDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/products")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO) throws URISyntaxException {
        log.debug("REST request to update Product : {}", productDTO);
        if (productDTO.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        ProductDTO result = productService.save(productDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code GET  /products} : get all the products.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of products in body.
     */
    @ApiOperation(value = "Get the list of available products, available sorting by property, default: by price, sorting direction: ascending"
    		, response = List.class)
    @GetMapping("/products")
    public List<ProductDTO> getAllProducts(@RequestParam(defaultValue = "price") String sortBy) {
        log.debug("REST request to get all Products");
        //return productService.findAll();
        return productService.findAll(sortBy);
    }

    /**
     * {@code GET  /products/search} : get all the products which name or description fields contains keyword.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of products in body.
     */
    @ApiOperation(value = "Search in fields name, description of products for specified keyword"
    		, response = List.class)
    @GetMapping("/products/search")
    public List<ProductDTO> searchProducts(@RequestParam String keyword) {
    	return productService.findByLikeNameOrDescription(keyword);
    }
    
    /**
     * {@code GET  /products/:id} : get the "id" product.
     *
     * @param id the id of the productDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        return ResponseEntity.ok(productService.findOne(id).orElseThrow(() -> new ProductNotFoundException(id)));
    }

    /**
     * {@code DELETE  /products/:id} : delete the "id" product.
     *
     * @param id the id of the productDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
