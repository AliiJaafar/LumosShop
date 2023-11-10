package com.dgpad;

import com.dgpad.category.CategoryService;
import com.lumosshop.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StoreController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/store")
    public String storeHome(Model model) {
        List<Category> categoryList = categoryService.findLeafCategories();
        model.addAttribute("categoryList", categoryList);

        return "product/store";
    }
}
