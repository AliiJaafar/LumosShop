package com.dgpad.admin.product;

import com.lumosshop.common.entity.Category;
import com.lumosshop.common.entity.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class productRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testImproveRatingsAndAverageScore() {
        Integer productID = 5;
        productRepository.improveRatingsAndAverageScore(productID);

    }

    @Test
    public void testCreateProduct() {
        Category category = entityManager.find(Category.class, 1);

        Product product3 = new Product();
        product3.setName("Thin-Film Flexible Solar Panel");
        product3.setAlias("Flexible Thin-Film Solar Panel");
        product3.setShortDescription("Lightweight and flexible solar panel for unique installation needs.");
        product3.setFullDescription("This Thin-Film Flexible Solar Panel is designed for unconventional solar installations. Its flexibility allows it to be mounted on curved surfaces, making it an excellent choice for boats, RVs, and other curved structures.");
        product3.setCategory(category);

        product3.setPrice(209); // Price in dollars
        product3.setCost(180); // Cost in dollars


        product3.setEnabled(true);
        product3.setInStock(true);

        product3.setCreatedTime(new Date());
        product3.setUpdatedTime(new Date());

        Product savedProduct = productRepository.save(product3);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllProducts() {
        Iterable<Product> iterableProducts = productRepository.findAll();

        iterableProducts.forEach(System.out::println);
    }

    @Test
    public void testGetProduct() {
        Integer id = 1;
        Product product = productRepository.findById(id).get();
        System.out.println(product);

        assertThat(product).isNotNull();
    }

    @Test
    public void testUpdateProduct() {
        Integer id = 1;
        Product product = productRepository.findById(id).get();
        product.setPrice(399);

        productRepository.save(product);

        Product updatedProduct = entityManager.find(Product.class, id);

        assertThat(updatedProduct.getPrice()).isEqualTo(399);
    }

    @Test
    public void testDeleteProduct() {
        Integer id = 3;
        productRepository.deleteById(id);

        Optional<Product> result = productRepository.findById(id);

        assertThat(!result.isPresent());
    }

    @Test
    public void testGetProductByName() {
        String name = "Thin-Film Flexible Solar Panel";
        Product productByName = productRepository.getProductByName(name);
        System.out.println("Is there A result -----?--> " + productByName);

        assertThat(productByName).isNotNull();

    }

    @Test
    public void testEnablingProduct() {
        productRepository.updateEnabledStatus(2, false);
        ;

    }
    @Test
    public void testListAllProductsByOrderByDiscount() {
        float percent = 1F;
        Iterable<Product> iterableProducts = productRepository.findAllByDiscountPercentGreaterThanOrderByDiscountPercentAsc(percent);

        iterableProducts.forEach(System.out::println);
    }
}
