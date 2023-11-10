package com.dgpad.category;

import com.lumosshop.common.entity.Category;
import com.lumosshop.common.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findLeafCategories() {
        List<Category> leafCategories = new ArrayList<>();
        List<Category> allEnableCategories = categoryRepository.findAllByEnabled();

        for (Category category : allEnableCategories) {
            if (isLeaf(category)) {
                leafCategories.add(category);
            }
        }

        return leafCategories;
    }

    private boolean isLeaf(Category category) {
        return category.getChildren() == null || category.getChildren().isEmpty();
    }


    public Category getCategoryByALias(String alias) throws CategoryNotFoundException {
        Category category = categoryRepository.findByAliasAndEnable(alias);
         if (category == null) {
            throw new CategoryNotFoundException("No categories were found matching the alias " + alias);
        }

        return category;
    }

    public List<Category> getParentCategories(Category childCategory) {
        List<Category> parentCategories = new ArrayList<>();


        while (childCategory != null) {
            parentCategories.add(0, childCategory);
            childCategory = childCategory.getParent();
        }

        return parentCategories;
    }


}
