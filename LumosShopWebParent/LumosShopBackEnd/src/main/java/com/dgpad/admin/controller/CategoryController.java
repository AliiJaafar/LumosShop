package com.dgpad.admin.controller;

import com.dgpad.admin.service.CategoryService;
import com.dgpad.admin.user.UserNotFoundException;
import com.dgpad.admin.util.CategoryPageInfo;
import com.dgpad.admin.util.FileUploadUtil;
import com.lumosshop.common.entity.Category;
import com.lumosshop.common.exception.CategoryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String listFirstPage(String sortDir, Model model) {
        return listByPage(1, "asc", null, model);
    }

    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
                             @Param("sortDir") String sortDirection,
                             @Param("keyword") String keyword,
                             Model model) {


        CategoryPageInfo pageInfo = new CategoryPageInfo();

        List<Category> listCategories = categoryService.listByPage(pageInfo, pageNum, sortDirection, keyword);

        long startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
        long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
        if (endCount > pageInfo.getTotalElements()) {
            endCount = pageInfo.getTotalElements();
        }

        String reverseSortDir = sortDirection.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", pageInfo.getTotalPages());
        model.addAttribute("totalElement", pageInfo.getTotalElements());
        model.addAttribute("currentPage", pageNum);

        model.addAttribute("sortField", "name");
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("reverseSortDirection", reverseSortDir);
        model.addAttribute("moduleURL", "/categories");


        System.out.println("totalPages : "+pageInfo.getTotalPages());
        System.out.println("totalElement :  " + pageInfo.getTotalElements());
        System.out.println("pageNum : "+ pageNum);





        return "category/categories";
    }


    @GetMapping("/categories/add")
    public String newCategory(Model model) {
        List<Category> categoryList = categoryService.listCategoriesUsedInForm();

        model.addAttribute("category", new Category());
        model.addAttribute("listCategories", categoryList);
        return "category/category-form";

    }

    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute("category") Category category,
                               RedirectAttributes redirectAttributes,
                               @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            category.setImage(fileName);

            Category savedCategory = categoryService.save(category);

			String uploadDir = "category-images/" + savedCategory.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            categoryService.save(category);
        }
        redirectAttributes.addFlashAttribute("messageSuccess", "The category has been saved successfully.");
        return "redirect:/categories";

    }
   @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id, Model model,
                               RedirectAttributes ra) {
        try {
            Category category = categoryService.get(id);
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();
            model.addAttribute("category", category);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");

            return "category/category-form";
        } catch (CategoryNotFoundException ex) {
            ra.addFlashAttribute("messageError", ex.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) throws UserNotFoundException {
        try {
            categoryService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Category ID " + id + " has been deleted from the system with success.");
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        // redirect to
        return "redirect:/categories";

    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String UpdateEnableStatus(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes) {

        categoryService.updateCategoryEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "We've updated the status for category ID " + id + " to " + status + ".";
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";

    }
}