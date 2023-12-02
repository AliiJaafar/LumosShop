package com.dgpad.admin.order;

import com.dgpad.admin.customer.CustomerService;
import com.dgpad.admin.product.ProductService;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private ProductService productService;
    @GetMapping("/orders/products-search")
    public String DisplaySearchForProductView(Model model) {

        return searchProductPaging(model, 1, "name", "asc", null);

    }

    /*@PostMapping("/orders/products-search")
    public String searchProducts(String keyword) {

        return "redirect:/orders/search_product/page/1?sortField=name&sortDir=asc&keyword=" + keyword;
    }*/


    @GetMapping("/orders/products-search/page/{pageNumber}")
    public String searchProductPaging(Model model,
                                      @PathVariable(name = "pageNumber") int pageNumber,
                                      @Param("sortField") String sortField,
                                      @Param("sortDirection") String sortDirection,
                                      @Param("keyword") String keyword) {

        Page<Product> page = productService.searchForProduct(pageNumber,sortField,sortDirection,keyword);
        List<Product> productList = page.getContent();
        long startCount = (long) (pageNumber - 1) * productService.SEARCH_RESULTS_PER_PAGE_IN_ORDER + 1;
        long endCount = startCount + productService.SEARCH_RESULTS_PER_PAGE_IN_ORDER - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElement", page.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("productList", productList);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", reverseSortDirection);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleURL", "/orders");

        return "order/search_for_product";
    }
}
