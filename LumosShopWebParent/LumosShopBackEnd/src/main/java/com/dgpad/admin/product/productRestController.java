package com.dgpad.admin.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class productRestController {

    @Autowired
    private ProductService productService;



    @PostMapping("/products/check_unique")

    public String checkIfProductNameIsUnique(Integer id, @RequestParam("name") String name) {
        boolean Unique = productService.isNameUnique(id, name);
        return Unique ? "OK" : "Duplicated";
    }

}
