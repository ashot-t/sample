package dev.shoppingcart.web.rest;

import dev.shoppingcart.domain.ProductOrder;
import dev.shoppingcart.domain.Product;
import dev.shoppingcart.repository.ProductOrderRepository;
import dev.shoppingcart.service.ProductOrderService;
import dev.shoppingcart.service.ProductService;
import dev.shoppingcart.service.dto.ProductOrderDTO;
import dev.shoppingcart.service.mapper.ProductOrderMapper;
import dev.shoppingcart.ShoppingCartApplication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.shoppingcart.domain.enumeration.OrderStatus;
/**
 * Integration tests for the {@Link ProductOrderResource} REST controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingCartApplication.class)
public class ProductOrderResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.NEW;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.PROCESSING;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private ProductOrderMapper productOrderMapper;

    @Autowired
    private ProductOrderService productOrderService;
    
    @Autowired
    private ProductService productService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restProductOrderMockMvc;

    private ProductOrder productOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductOrderResource productOrderResource = new ProductOrderResource(productOrderService, productService);
        this.restProductOrderMockMvc = MockMvcBuilders.standaloneSetup(productOrderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOrder createEntity(EntityManager em) {
        ProductOrder productOrder = new ProductOrder()
            .orderDate(DEFAULT_ORDER_DATE)
            .price(DEFAULT_PRICE)
            .status(DEFAULT_STATUS);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        productOrder.setProduct(product);
        return productOrder;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOrder createUpdatedEntity(EntityManager em) {
        ProductOrder productOrder = new ProductOrder()
            .orderDate(UPDATED_ORDER_DATE)
            .price(UPDATED_PRICE)
            .status(UPDATED_STATUS);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        productOrder.setProduct(product);
        return productOrder;
    }

    @Before
    public void initTest() {
        productOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductOrder() throws Exception {
        int databaseSizeBeforeCreate = productOrderRepository.findAll().size();

        // Create the ProductOrder
        /*
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);
        restProductOrderMockMvc.perform(post("/api/product-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOrderDTO)))
            .andExpect(status().isCreated());
        
        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeCreate + 1);
        ProductOrder testProductOrder = productOrderList.get(productOrderList.size() - 1);
        assertThat(testProductOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testProductOrder.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProductOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        */

        // Create the ProductOrder
        final Long productId = 2L;
        Product product = productService.findById(productId).get();
        ProductOrderRequest request = new ProductOrderRequest(productId);
        restProductOrderMockMvc.perform(post("/api/product-orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isCreated());
        
        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeCreate + 1);
        ProductOrder testProductOrder = productOrderList.get(productOrderList.size() - 1);
        assertThat(testProductOrder.getPrice()).isEqualTo(product.getPrice());
        assertThat(testProductOrder.getStatus()).isEqualTo(OrderStatus.NEW);
    }

    /*
    @Test
    @Transactional
    public void createProductOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productOrderRepository.findAll().size();

        // Create the ProductOrder with an existing ID
        productOrder.setId(1L);
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductOrderMockMvc.perform(post("/api/product-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeCreate);
    }
    */


    @Test
    @Transactional
    public void getAllProductOrders() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList
        restProductOrderMockMvc.perform(get("/api/product-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getProductOrder() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        // Get the productOrder
        restProductOrderMockMvc.perform(get("/api/product-orders/{id}", productOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductOrder() throws Exception {
        // Get the productOrder
        restProductOrderMockMvc.perform(get("/api/product-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductOrder() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        int databaseSizeBeforeUpdate = productOrderRepository.findAll().size();

        // Update the productOrder
        ProductOrder updatedProductOrder = productOrderRepository.findById(productOrder.getId()).get();
        // Disconnect from session so that the updates on updatedProductOrder are not directly saved in db
        em.detach(updatedProductOrder);
        updatedProductOrder
            .orderDate(UPDATED_ORDER_DATE)
            .price(UPDATED_PRICE)
            .status(UPDATED_STATUS);
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(updatedProductOrder);

        restProductOrderMockMvc.perform(put("/api/product-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOrderDTO)))
            .andExpect(status().isOk());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
        ProductOrder testProductOrder = productOrderList.get(productOrderList.size() - 1);
        assertThat(testProductOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testProductOrder.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProductOrder.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = productOrderRepository.findAll().size();

        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOrderMockMvc.perform(put("/api/product-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductOrder() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        int databaseSizeBeforeDelete = productOrderRepository.findAll().size();

        // Delete the productOrder
        restProductOrderMockMvc.perform(delete("/api/product-orders/{id}", productOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(productOrderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(productOrderMapper.fromId(null)).isNull();
    }
}
