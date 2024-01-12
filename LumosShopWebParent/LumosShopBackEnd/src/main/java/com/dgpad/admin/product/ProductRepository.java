package com.dgpad.admin.product;

import com.lumosshop.common.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer>, PagingAndSortingRepository<Product, Integer> {


    @Query("update Product p set " +
            "p.rating_avg = CAST(COALESCE((select avg(r.rating) from Review r where r.product.id = ?1), 0) AS java.lang.Float), " +
            "p.reviews = (select count(r.id) from Review r where r.product.id = ?1) " +
            "where p.id = ?1")
    @Modifying
    public void improveRatingsAndAverageScore(Integer productId);

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


    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    public Page<Product> searchProductsByName(String keyword, Pageable pageable);

    public List<Product> findAllByOrderByDiscountPercentDesc();

    @Query("SELECT p FROM Product p WHERE p.discountPercent > ?1 ORDER BY p.discountPercent ASC")
    List<Product> findAllByDiscountPercentGreaterThanOrderByDiscountPercentAsc(float percent);
}