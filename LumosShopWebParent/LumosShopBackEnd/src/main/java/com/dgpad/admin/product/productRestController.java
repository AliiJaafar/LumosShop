package com.dgpad.admin.product;

import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class productRestController {

    @Autowired
    private ProductService productService;



    @PostMapping("/products/check_unique")

    public String checkIfProductNameIsUnique(Integer id, @RequestParam("name") String name) {
        boolean Unique = productService.isNameUnique(id, name);
        return Unique ? "OK" : "Duplicated";
    }


    @GetMapping("/products/get/{id}")
    public ProductDTO getProductInfo(@PathVariable("id") Integer id)
            throws ProductNotFoundException {

        Product product = productService.findProductById(id);

        return new ProductDTO(product.getName(), product.getMainImagePath(),
                product.getDiscountPrice(), product.getCost());
    }
}
