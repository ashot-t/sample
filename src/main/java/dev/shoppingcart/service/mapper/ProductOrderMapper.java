package dev.shoppingcart.service.mapper;

import dev.shoppingcart.domain.*;
import dev.shoppingcart.service.dto.ProductOrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductOrder} and its DTO {@link ProductOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ProductOrderMapper extends EntityMapper<ProductOrderDTO, ProductOrder> {
	/*
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    ProductOrderDTO toDto(ProductOrder productOrder);

    @Mapping(source = "productId", target = "product")
    ProductOrder toEntity(ProductOrderDTO productOrderDTO);
	*/
    default ProductOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductOrder productOrder = new ProductOrder();
        productOrder.setId(id);
        return productOrder;
    }
}
