package dev.shoppingcart.web.rest;

import dev.shoppingcart.domain.Product;
import dev.shoppingcart.domain.ProductOrder;
import dev.shoppingcart.domain.enumeration.OrderStatus;
import dev.shoppingcart.service.ProductOrderService;
import dev.shoppingcart.service.ProductService;
import dev.shoppingcart.web.rest.errors.BadRequestException;
import dev.shoppingcart.web.rest.errors.OrderNotFoundException;
import dev.shoppingcart.web.rest.errors.ProductNotFoundException;
import io.swagger.annotations.ApiOperation;
import dev.shoppingcart.service.dto.ProductDTO;
import dev.shoppingcart.service.dto.ProductOrderDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;

/**
 * REST controller for managing {@link cart.shopping.domain.ProductOrder}.
 */
@RestController
@RequestMapping("/api")
public class ProductOrderResource {

    private final Logger log = LoggerFactory.getLogger(ProductOrderResource.class);

    private final ProductOrderService productOrderService;
    
    private final ProductService productService;

    public ProductOrderResource(ProductOrderService productOrderService, ProductService productService) {
        this.productOrderService = productOrderService;
        this.productService = productService;
    }

    /**
     * {@code POST  /product-orders} : Create a new productOrder.
     *
     * @param productOrderDTO the productOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productOrderDTO, or with status {@code 400 (Bad Request)} if the productOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*
    @PostMapping("/product-orders")
    public ResponseEntity<ProductOrderDTO> createProductOrder(@Valid @RequestBody ProductOrderDTO productOrderDTO) throws URISyntaxException {
        log.debug("REST request to save ProductOrder : {}", productOrderDTO);
        if (productOrderDTO.getId() != null) {
            throw new BadRequestException("A new productOrder cannot already have an ID");
        }
        ProductOrderDTO result = productOrderService.save(productOrderDTO);
        return ResponseEntity.created(new URI("/api/product-orders/" + result.getId()))
            .body(result);
    }
    */
    @Transactional
    @PostMapping("/product-orders")
    public ResponseEntity<ProductOrderDTO> createProductOrder(@Valid @RequestBody ProductOrderRequest req) throws URISyntaxException {
    	log.debug("REST request to save ProductOrderRequest : {}", req);
        if (req.getProductId() == null) {
            throw new BadRequestException("Please provide Product ID !!!");
        }
        Product product = productService.findById(req.getProductId())
        		.orElseThrow(() -> new ProductNotFoundException(req.getProductId()));
        ProductOrder productOrder = new ProductOrder();
        productOrder.setPrice(product.getPrice());
        productOrder.setStatus(OrderStatus.NEW);
        productOrder.setProduct(product);
        productService.decreaseStockCount(req.getProductId());
        ProductOrderDTO saved = productOrderService.save(productOrder);
        return ResponseEntity.created(new URI("/api/product-orders/" + saved.getId())).body(saved);
    }
    
    /**
     * {@code PUT  /product-orders} : Updates an existing productOrder.
     *
     * @param productOrderDTO the productOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productOrderDTO,
     * or with status {@code 400 (Bad Request)} if the productOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-orders")
    public ResponseEntity<ProductOrderDTO> updateProductOrder(@Valid @RequestBody ProductOrderDTO productOrderDTO) throws URISyntaxException {
        log.debug("REST request to update ProductOrder : {}", productOrderDTO);
        if (productOrderDTO.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        ProductOrderDTO result = productOrderService.save(productOrderDTO);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "Change status of product order, acceptable values are - (NEW, PROCESSING, COMPLETED, CANCELED)"
    		, response = ProductOrderDTO.class)
    @PutMapping("/product-orders/{id}/changeStatus")
    public ResponseEntity<ProductOrderDTO> updateProductOrderStatus(@PathVariable Long id, @RequestBody OrderStatus status) throws URISyntaxException {
        log.debug("REST request to update ProductOrder status: {}", status);
        if (id == null) {
            throw new BadRequestException("Invalid id");
        }
        ProductOrderDTO productOrderDTO = productOrderService.findOne(id).orElseThrow(() -> new OrderNotFoundException(id));
        productOrderDTO.setStatus(status);
        ProductOrderDTO result = productOrderService.save(productOrderDTO);
        return ResponseEntity.ok().body(result);
    }

    
    /**
     * {@code GET  /product-orders} : get all the productOrders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productOrders in body.
     */
    @GetMapping("/product-orders")
    public List<ProductOrderDTO> getAllProductOrders() {
        log.debug("REST request to get all ProductOrders");
        return productOrderService.findAll();
    }

    /**
     * {@code GET  /product-orders/:id} : get the "id" productOrder.
     *
     * @param id the id of the productOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-orders/{id}")
    public ResponseEntity<ProductOrderDTO> getProductOrder(@PathVariable Long id) {
        log.debug("REST request to get ProductOrder : {}", id);
        return ResponseEntity.ok(productOrderService.findOne(id)
        						.orElseThrow(() -> new OrderNotFoundException(id)));
    }

    /**
     * {@code DELETE  /product-orders/:id} : delete the "id" productOrder.
     *
     * @param id the id of the productOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-orders/{id}")
    public ResponseEntity<Void> deleteProductOrder(@PathVariable Long id) {
        log.debug("REST request to delete ProductOrder : {}", id);
        productOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
