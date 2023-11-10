package com.dgpad.admin.service.Impl;

import com.dgpad.admin.util.CategoryPageInfo;
import com.lumosshop.common.entity.Category;
import com.lumosshop.common.exception.CategoryNotFoundException;

import java.util.List;

public interface ICategoryService {
    public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum, String sortDir,
                                     String keyword);

    public List<Category> listCategoriesUsedInForm();

    public Category save(Category category);

    public Category get(Integer id) throws CategoryNotFoundException, CategoryNotFoundException;

    public void updateCategoryEnabledStatus(Integer id, boolean enabled);

    public void delete(Integer id) throws CategoryNotFoundException;
}
