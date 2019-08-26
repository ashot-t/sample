package dev.shoppingcart.service.mapper;

import dev.shoppingcart.domain.Product;
import dev.shoppingcart.domain.ProductOrder;
import dev.shoppingcart.service.dto.ProductOrderDTO;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductOrderMapperImpl implements ProductOrderMapper {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<ProductOrder> toEntity(List<ProductOrderDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ProductOrder> list = new ArrayList<ProductOrder>( dtoList.size() );
        for ( ProductOrderDTO productOrderDTO : dtoList ) {
            list.add( toEntity( productOrderDTO ) );
        }

        return list;
    }

    @Override
    public List<ProductOrderDTO> toDto(List<ProductOrder> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ProductOrderDTO> list = new ArrayList<ProductOrderDTO>( entityList.size() );
        for ( ProductOrder productOrder : entityList ) {
            list.add( toDto( productOrder ) );
        }

        return list;
    }

    @Override
    public ProductOrderDTO toDto(ProductOrder productOrder) {
        if ( productOrder == null ) {
            return null;
        }

        ProductOrderDTO productOrderDTO = new ProductOrderDTO();

        productOrderDTO.setProductId( productOrderProductId( productOrder ) );
        productOrderDTO.setProductName( productOrderProductName( productOrder ) );
        productOrderDTO.setId( productOrder.getId() );
        productOrderDTO.setOrderDate( productOrder.getOrderDate() );
        productOrderDTO.setPrice( productOrder.getPrice() );
        productOrderDTO.setStatus( productOrder.getStatus() );

        return productOrderDTO;
    }

    @Override
    public ProductOrder toEntity(ProductOrderDTO productOrderDTO) {
        if ( productOrderDTO == null ) {
            return null;
        }

        ProductOrder productOrder = new ProductOrder();

        productOrder.setProduct( productMapper.fromId( productOrderDTO.getProductId() ) );
        productOrder.setId( productOrderDTO.getId() );
        productOrder.setOrderDate( productOrderDTO.getOrderDate() );
        productOrder.setPrice( productOrderDTO.getPrice() );
        productOrder.setStatus( productOrderDTO.getStatus() );

        return productOrder;
    }

    private Long productOrderProductId(ProductOrder productOrder) {
        if ( productOrder == null ) {
            return null;
        }
        Product product = productOrder.getProduct();
        if ( product == null ) {
            return null;
        }
        Long id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String productOrderProductName(ProductOrder productOrder) {
        if ( productOrder == null ) {
            return null;
        }
        Product product = productOrder.getProduct();
        if ( product == null ) {
            return null;
        }
        String name = product.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
