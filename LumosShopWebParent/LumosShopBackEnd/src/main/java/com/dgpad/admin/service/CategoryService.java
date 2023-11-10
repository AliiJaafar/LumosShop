package com.dgpad.admin.service;

import com.dgpad.admin.CategoryRepository;
import com.dgpad.admin.service.Impl.ICategoryService;
import com.dgpad.admin.util.CategoryPageInfo;
import com.lumosshop.common.entity.Category;
import com.lumosshop.common.exception.CategoryNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class CategoryService implements ICategoryService {
    public static final int ROOT_CATEGORIES_PER_PAGE = 4;

    @Autowired
    private CategoryRepository repo;

    public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum, String sortDirection, String keyword) {
        Sort sort = Sort.by("name");

        if (sortDirection.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDirection.equals("desc")) {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, ROOT_CATEGORIES_PER_PAGE, sort);

        Page<Category> pageCategories = null;

        if (keyword != null && !keyword.isEmpty()) {
            pageCategories = repo.search(keyword, pageable);
        } else {
            pageCategories = repo.findRootCategories(pageable);
        }

        List<Category> rootCategories = pageCategories.getContent();

        pageInfo.setTotalElements(pageCategories.getTotalElements());
        pageInfo.setTotalPages(pageCategories.getTotalPages());

        if (keyword != null && !keyword.isEmpty()) {
            List<Category> searchResult = pageCategories.getContent();
            for (Category category : searchResult) {
                category.setHasChildren(category.getChildren().size() > 0);
            }

            return searchResult;

        } else {
            return listHierarchicalCategories(rootCategories, sortDirection);
        }
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

            for (Category subCategory : children) {
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory, name));

                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
            }
        }

        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel, String sortDir) {
        Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
        int newSubLevel = subLevel + 1;

        for (Category subCategory : children) {
            String name = "--".repeat(Math.max(0, newSubLevel)) + subCategory.getName();

            hierarchicalCategories.add(Category.copyFull(subCategory, name));

            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
        }

    }

    public Category save(Category category) {
        Category parent = category.getParent();
        if (parent != null) {
            String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
            allParentIds += parent.getId() + "-";
            category.setAllParentIDs(allParentIds);
        }

        return repo.save(category);
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = repo.findRootCategories(Sort.by("name").ascending());

        for (Category category : categoriesInDB) {
            categoriesUsedInForm.add(Category.copyIdAndName(category));

            Set<Category> children = sortSubCategories(category.getChildren());

            for (Category subCategory : children) {
                String name = "--" + subCategory.getName();
                categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

                listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
            }
        }

        return categoriesUsedInForm;
    }

    private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = sortSubCategories(parent.getChildren());

        for (Category subCategory : children) {
            String name = "--".repeat(Math.max(0, newSubLevel)) + subCategory.getName();
            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
            listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
        }
    }

    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }



    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, "asc");
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>((cat1, cat2) -> {
            if (sortDir.equals("asc")) {
                return cat1.getName().compareTo(cat2.getName());
            } else {
                return cat2.getName().compareTo(cat1.getName());
            }
        });

        sortedChildren.addAll(children);

        return sortedChildren;
    }


    public boolean isCategoryNameAndAliasUnique(Integer id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);
        Category categoryByName = repo.findByName(name);

        if (isCreatingNew) {
            if (categoryByName != null) {
                return false;
            } else {
                Category categoryByAlias = repo.findByAlias(alias);
                if (categoryByAlias != null) {
                    return false;
                }
            }
        } else {
            if (categoryByName != null && categoryByName.getId() != id) {
                return false;
            }

            Category categoryByAlias = repo.findByAlias(alias);
            if (categoryByAlias != null && categoryByAlias.getId() != id) {
                return false;
            }

        }

        return true;
    }



    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
        repo.updateEnabledStatus(id, enabled);
    }

    public void delete(Integer id) throws CategoryNotFoundException {
        Long countById = repo.countById(id);
        if (countById == null || countById == 0) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }

        repo.deleteById(id);
    }
    public List<Category> findAll() {
        Iterable<Category> categoryList = repo.findAll();
        return (List<Category>) categoryList;
    }


}
