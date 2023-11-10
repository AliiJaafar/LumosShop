package com.dgpad.admin.product;

import com.lumosshop.common.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends CrudRepository<Product, Integer>, PagingAndSortingRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.name = :name")
    public Product getProductByName(@Param("name") String name);

    @Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);

    public Long countById(Integer id);

    @Query("select p from Product p where p.name like %?1%" +
            "or p.alias like %?1%" +
            "or p.category.name like %?1%" +
            "or p.shortDescription like %?1%" +
            "or p.fullDescription like %?1%")
    public Page<Product> findAll(String keyword, Pageable pageable);


    @Query("select p from Product p where p.category.id = ?1 or " +
            "p.category.allParentIDs like %?2%")
    public Page<Product> findAllByCategory(Integer categoryId, String retrieveCategoryMatchingId, Pageable pageable);

}