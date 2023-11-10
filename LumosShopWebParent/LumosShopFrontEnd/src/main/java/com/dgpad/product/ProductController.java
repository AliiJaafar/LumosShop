package com.dgpad.product;

import com.dgpad.category.CategoryService;
import com.lumosshop.common.entity.Category;
import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.exception.CategoryNotFoundException;
import com.lumosshop.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("store")
public class ProductController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @GetMapping("/c/{category_alias}")
    public String displayCategoryFirstPage(@PathVariable("category_alias") String alias,
                                           Model model) throws CategoryNotFoundException {


        return displayCategory(alias, model, 1);
    }

    @GetMapping("/c/{category_alias}/page/{pageNumber}")
    public String displayCategory(@PathVariable("category_alias") String alias,
                                  Model model,
                                  @PathVariable("pageNumber") int pageNumber) throws CategoryNotFoundException {

        try {
            Category category = categoryService.getCategoryByALias(alias);

            List<Category> ChainCategory = categoryService.getParentCategories(category);
            Page<Product> productPage = productService.listProductsByTheirCategory(pageNumber, category.getId());
            long startCount = (pageNumber - 1) * productService.ELEMENT_PER_PAGE + 1;
            long endCount = startCount + productService.ELEMENT_PER_PAGE - 1;
            if (endCount > productPage.getTotalElements()) {
                endCount = productPage.getTotalElements();
            }
            List<Product> productList = productPage.getContent();

            model.addAttribute("currentPage", pageNumber);
            model.addAttribute("totalPages", productPage.getTotalPages());
            model.addAttribute("totalElement", productPage.getTotalElements());
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);


            model.addAttribute("category", category);
            model.addAttribute("pageTitle", category.getName());
            model.addAttribute("ChainCategory", ChainCategory);
            model.addAttribute("productList", productList);


            return "product/list-the-products-by-category";
        } catch (CategoryNotFoundException e) {
            return "error/404";

        }

    }

    @GetMapping("/p/{product-alias}")
    public String displayProduct(@PathVariable("product-alias") String alias,
                                 Model model) {
        try {
            Product product = productService.getProduct(alias);
            List<Category> ChainCategory = categoryService.getParentCategories(product.getCategory());

            model.addAttribute("ChainCategory", ChainCategory);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", product.getName());

            return "product/product-page";

        } catch (ProductNotFoundException exception) {
            return "error/404";
        }
    }

    @GetMapping("/search")
    public String searchFirstPage(@RequestParam("keyword") String keyword, Model model) {
        return search(keyword, model, 1);
    }
    @GetMapping("/search/page/{pageNumber}")
    public String search(@Param("keyword") String keyword, Model model,
                         @PathVariable("pageNumber")int pageNumber) {

        Page<Product> productPage = productService.search(keyword, pageNumber);
        List<Product> productList = productPage.getContent();

        long startCount = (pageNumber - 1) * ProductService.SEARCH_RESULTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1;
        if (endCount > productPage.getTotalElements()) {
            endCount = productPage.getTotalElements();
        }
        System.out.println("currentPage: "+ pageNumber);
        System.out.println("totalPages: "+ productPage.getTotalPages());
        System.out.println("startCount: "+ startCount);
        System.out.println("endCount: "+ endCount);
        System.out.println("totalElement: "+ productPage.getTotalElements());
        System.out.println("keyword: " + keyword);

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalElement", productPage.getTotalElements());
        model.addAttribute("pageTitle", keyword + " - Search Hits");


        model.addAttribute("keyword", keyword);
        model.addAttribute("productList", productList);
        return "product/searchHits";
    }
}
