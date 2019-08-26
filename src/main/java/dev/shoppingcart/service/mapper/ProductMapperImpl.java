package dev.shoppingcart.service.mapper;

import dev.shoppingcart.domain.Product;
import dev.shoppingcart.service.dto.ProductDTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ProductMapperImpl implements ProductMapper {
	
    @Override
    public Product toEntity(ProductDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Product product = new Product();

        product.setId( dto.getId() );
        product.setName( dto.getName() );
        product.setDescription( dto.getDescription() );
        product.setType( dto.getType() );
        product.setPrice( dto.getPrice() );
        product.setCountInStock( dto.getCountInStock() );
        product.setCreatedDate( dto.getCreatedDate() );
        product.setLastModifiedDate( dto.getLastModifiedDate() );

        return product;
    }

    @Override
    public ProductDTO toDto(Product entity) {
        if ( entity == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setId( entity.getId() );
        productDTO.setName( entity.getName() );
        productDTO.setDescription( entity.getDescription() );
        productDTO.setType( entity.getType() );
        productDTO.setPrice( entity.getPrice() );
        productDTO.setCountInStock( entity.getCountInStock() );
        productDTO.setCreatedDate( entity.getCreatedDate() );
        productDTO.setLastModifiedDate( entity.getLastModifiedDate() );

        return productDTO;
    }

    @Override
    public List<Product> toEntity(List<ProductDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Product> list = new ArrayList<Product>( dtoList.size() );
        for ( ProductDTO productDTO : dtoList ) {
            list.add( toEntity( productDTO ) );
        }

        return list;
    }

    @Override
    public List<ProductDTO> toDto(List<Product> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ProductDTO> list = new ArrayList<ProductDTO>( entityList.size() );
        for ( Product product : entityList ) {
            list.add( toDto( product ) );
        }

        return list;
    }
}
