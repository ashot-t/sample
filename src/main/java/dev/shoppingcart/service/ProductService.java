package dev.shoppingcart.service;

import dev.shoppingcart.domain.Product;
import dev.shoppingcart.repository.ProductRepository;
import dev.shoppingcart.service.dto.ProductDTO;
import dev.shoppingcart.service.mapper.ProductMapper;
import dev.shoppingcart.web.rest.errors.ProductNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

	@Transactional
	public void decreaseStockCount(Long id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
		if(product.getCountInStock() < 1) {
			throw new RuntimeException("There isn't product in stock !!!");
		}
		product.setCountInStock(product.getCountInStock() - 1);
		productRepository.save(product);
	}
    
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Save a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    /**
     * Get all the products.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        log.debug("Request to get all Products");
        return productRepository.findAll().stream()
            .map(productMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional
    public List<ProductDTO> findAll(String sortBy) {
        log.debug("Request to get all Products");
    	return productRepository.findAll(Sort.by(sortBy).ascending()).stream()
    			.map(productMapper::toDto)
    			.collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one product by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id)
            .map(productMapper::toDto);
    }
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id);
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }

    /**
     * Find products which contains specified keyword in name or description fiels.
     * @param keyword the keyword to search.
     * @return List of ProductDtos 
     */
	public List<ProductDTO> findByLikeNameOrDescription(String keyword) {
		return productRepository.findAll().stream()
				.filter(p -> (p.getName().toLowerCase().contains(keyword.toLowerCase()) || 
						p.getDescription().toLowerCase().contains(keyword.toLowerCase())))
				.map(productMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
	}
}
