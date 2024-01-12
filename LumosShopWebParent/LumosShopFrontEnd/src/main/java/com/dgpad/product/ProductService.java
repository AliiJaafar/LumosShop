package com.dgpad.product;

import com.dgpad.interactions.InteractionRepository;
import com.dgpad.order.OrderSummaryRepository;
import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.entity.review.Review;
import com.lumosshop.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderSummaryRepository orderSummaryRepository;
    @Autowired
    private InteractionRepository interactionRepository;

    public static final int SEARCH_RESULTS_PER_PAGE = 10;

    public static final int ELEMENT_PER_PAGE = 12;


    public Page<Product> listProductsByTheirCategory(int pageNumber,
                                                     Integer categoryId) {
        String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
        Pageable pageable = PageRequest.of(pageNumber - 1, ELEMENT_PER_PAGE);

        return productRepository.listingByCategory(categoryId, categoryIdMatch, pageable);
    }

    public Product getProduct(String alias) throws ProductNotFoundException {
        Product product = productRepository.findByAlias(alias);
        if (product == null) {
            throw new ProductNotFoundException("could not found this Product with alias - " + alias);
        }
        return product;
    }
    public Page<Product> search(String keyword, int pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber - 1, SEARCH_RESULTS_PER_PAGE);
        return productRepository.search(keyword, pageable);
    }

    public Product retrieveProduct(Integer ID) throws ProductNotFoundException {
        try {
            return productRepository.findById(ID).get();
        } catch (NoSuchElementException e) {
            throw new ProductNotFoundException("could not found this Product with ID - " + ID);
        }

    }

    public List<Product> listAllProducts() {
        return (List<Product>) productRepository.findAll();
    }


    public List<Product> displayMostInSaleProducts() {
        float percent = 2F;

        return productRepository.findAllByDiscountPercentGreaterThanOrderByDiscountPercentAsc(percent);

    }


    public List<Product> displayMostClickedProducts(Integer limit) {
        return interactionRepository.findMostClickedProduct(limit);

    }
    public List<Product> displayMostSellerProducts(Integer limit) {
        return orderSummaryRepository.findMostSellerProductDesc(limit);

    }
}