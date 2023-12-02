package com.dgpad.admin.product;

import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional

public class ProductService {
    public static final int PRODUCT_PER_PAGE = 5;
    public static final int SEARCH_RESULTS_PER_PAGE_IN_ORDER = 4;

    @Autowired
    private ProductRepository productRepository;

    public List<Product> listAll() {
        return (List<Product>) productRepository.findAll();
    }


    public Product save(Product product) {
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }

        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String defaultAlias = product.getName().replaceAll(" ", "_");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().replaceAll(" ", "_"));
        }

        product.setUpdatedTime(new Date());

        Product savedProduct = productRepository.save(product);

        return savedProduct;
    }

    public boolean isNameUnique(Integer id, String name) {

        // Check if the name already exists in the database
        Product productByName = productRepository.getProductByName(name);

        if (productByName == null) return true; // The name is unique, return true

        boolean isCreatingNew = (id == null); // Check if creating a new product or updating an existing one

        if (isCreatingNew) {
            // Creating a new product, but the name already exists, return false
            return false;
        } else {
            // Updating an existing product, check if the name belongs to the same product
            return productByName.getId().equals(id); // Return true if the same user, false otherwise
        }

    }

    public void deleteById(int id) throws ProductNotFoundException {

        Long countById = productRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new ProductNotFoundException("could not found this Product id - " + id);
        }
        productRepository.deleteById(id);

    }
    public void updateProductEnabledStatus(Integer id, boolean enabled) {
        productRepository.updateEnabledStatus(id,enabled);
    }

    public Product findProductById(int id) throws ProductNotFoundException {
        try {
            return productRepository.findById(id).get();

        } catch (NoSuchElementException e) {
            throw new ProductNotFoundException("Could not found this Product id :  " + id);
        }

    }

    public Page<Product> listByPage(int pageNum, String sortField, String sortDirection, String keyword, Integer categoryId) {

        Sort sort = Sort.by(sortField);
        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();


        Pageable pageable = PageRequest.of(pageNum - 1, 5, sort);


        if (keyword != null && !keyword.isEmpty()) {

            return productRepository.findAll(keyword, pageable);
        }
        if (categoryId != null && categoryId > 0) {
            String retrieveCategoryID = "-" + String.valueOf(categoryId) + "-";
            return productRepository.findAllByCategory(categoryId, retrieveCategoryID, pageable);
        }
        return productRepository.findAll(pageable);

    }

    public Page<Product> searchForProduct(int pageNum, String sortField, String sortDirection, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE_IN_ORDER, sort);

        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.searchProductsByName(keyword, pageable);
        }
        return productRepository.findAll(pageable);
    }
   /*public void searchForProduct(int pageNum, String sortField, String sortDirection, String keyword) {
       Sort sort = Sort.by(sortField);
       sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();

       Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE_IN_ORDER, sort);

       Page<Product> page = null;

       if (keyword != null && !keyword.isEmpty()) {
            page = productRepository.searchProductsByName(keyword, pageable);
       }

   }*/



}
