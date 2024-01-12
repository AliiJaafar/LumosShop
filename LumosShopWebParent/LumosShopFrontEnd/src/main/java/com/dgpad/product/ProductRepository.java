package com.dgpad.product;

import com.lumosshop.common.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>, CrudRepository<Product, Integer> {
    @Query("update Product p set " +
            "p.rating_avg = CAST(COALESCE((select avg(r.rating) from Review r where r.product.id = ?1), 0) AS java.lang.Float), " +
            "p.reviews = (select count(r.id) from Review r where r.product.id = ?1) " +
            "where p.id = ?1")
    @Modifying
    public void improveRatingsAndAverageScore(Integer productId);
    @Query("select p from Product p where p.enabled = true "
            + "and (p.category.id = ?1 or p.category.allParentIDs like %?2%)"
            + " order by p.name asc ")
    public Page<Product> listingByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);


    @Query(value = "select * from products where enabled = true "+
            "and "
            +"match(name, short_description, full_description) " +
            "against (?1)",
            nativeQuery = true)
    public Page<Product> search(String keyword, Pageable pageable);

    public Product findByAlias(String alias);

    @Query("SELECT p FROM Product p WHERE p.discountPercent > ?1 ORDER BY p.discountPercent ASC")
    List<Product> findAllByDiscountPercentGreaterThanOrderByDiscountPercentAsc(float percent);


    List<Product> findByNameIn(List<String> names);
}
