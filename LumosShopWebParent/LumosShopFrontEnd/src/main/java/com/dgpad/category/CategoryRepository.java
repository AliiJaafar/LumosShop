package com.dgpad.category;
import com.lumosshop.common.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

    @Query("select c from Category c where c.enabled = true order by c.name asc")
    public List<Category> findAllByEnabled();

    @Query("SELECT c FROM Category c WHERE c.enabled = true AND c.alias = ?1")
    public Category findByAliasAndEnable(String alias);
}
