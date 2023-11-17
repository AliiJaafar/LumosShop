package com.dgpad.admin.category;

import ch.qos.logback.core.read.ListAppender;
import com.dgpad.admin.CategoryRepository;
import com.lumosshop.common.entity.Category;
import org.glassfish.jaxb.runtime.v2.schemagen.xmlschema.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Set;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCreateRootCategory() {
        Category category = new Category("Solar Panel");
        Category category2 = new Category("Inverter");
        Category category3 = new Category("Solar Accessories");
        categoryRepository.save(category3);
    }


    @Test
    public void testCreateSubCategory() {
        Category Cable = new Category(12);
        Category subCategory = new Category("DC Solar Cables", Cable);
        Category subCategory2 = new Category("AC Solar Cables", Cable);
        Category monitoringDevice = new Category(18);

        Category subCategory3 = new Category("Solar Power Meters", monitoringDevice);
        Category subCategory4 = new Category("Weather Sensors", monitoringDevice);



//        categoryRepository.save(subCategory);
//        categoryRepository.save(subCategory2);
        categoryRepository.save(subCategory3);
        categoryRepository.save(subCategory4);
    }

    @Test
    public void testGetCategoryName() {
        Category category = categoryRepository.findById(6).get();
        System.out.println(category.getName());
        Set<Category> children = category.getChildren();
        for (Category subCategory : children) {
            System.out.println(subCategory.getName());

        }

    }

    @Test
    public void testPrintHierarchicalCategories() {
        Iterable<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            if (category.getParent() == null) {
                System.out.println(category.getName());

                Set<Category> children = category.getChildren();

                for (Category subCategory : children) {
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);

                }
            }
        }
    }
    private void printChildren(Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory : children) {
            for (int i = 0; i < newSubLevel; i++) {
                System.out.print("--");
            }

            System.out.println(subCategory.getName());

            printChildren(subCategory, newSubLevel);
        }
    }
}
