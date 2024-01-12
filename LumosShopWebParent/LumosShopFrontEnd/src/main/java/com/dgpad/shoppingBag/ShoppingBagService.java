package com.dgpad.shoppingBag;

import com.dgpad.product.ProductRepository;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.ShoppingBag;
import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.exception.ShoppingBagException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Transient;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class ShoppingBagService {
    final Integer MAXIMUM_ALLOWED_ITEMS_PER_PRODUCT = 6;

    @Autowired
    private ShoppingBagRepository bagRepository;
    @Autowired
    private ProductRepository productRepository;


    public void deleteProductFromBag(Customer customer, Integer productId) {
        bagRepository.deleteByCustomerAndProduct(productId, customer.getId());
    }

    public void deleteByTheCustomer(Customer customer) {
        bagRepository.deleteByCustomer(customer.getId());
    }
    public List<ShoppingBag> bagList(Customer customer) {
        return bagRepository.findByCustomer(customer);
    }
    public float updateQuantity(Integer productId, Integer quantity, Customer customer) {


        bagRepository.changeQuantity(customer.getId(), productId, quantity);

        Product product = productRepository.findById(productId).get();

        //Total of the products multiply by Qty
        return product.getDiscountPrice() * quantity;

    }

    public Integer addProduct(Integer productID, Customer customer, Integer Qty) throws ShoppingBagException {
        Integer targetQty = Qty;

        Product product = new Product(productID);

        ShoppingBag selectedItems = bagRepository.findByCustomerAndProduct(customer, product);

        if (selectedItems != null) {
            targetQty = selectedItems.getQuantity() + Qty;
            if (targetQty > MAXIMUM_ALLOWED_ITEMS_PER_PRODUCT) {
                throw new ShoppingBagException("Oops, you can't stash more than " +
                        Qty + " of these, 'cause your cart's already holding "
                        + selectedItems.getQuantity() + ". You're limited to a maximum of "
                        + MAXIMUM_ALLOWED_ITEMS_PER_PRODUCT+".");
            }
        } else {
            selectedItems = new ShoppingBag();
            selectedItems.setProduct(product);
            selectedItems.setCustomer(customer);
        }
        selectedItems.setQuantity(targetQty);

        bagRepository.save(selectedItems);
        return targetQty;
    }

    public List<Product> AllProductInCartForCustomer(Integer CustomerID) {
        return bagRepository.InCartProducts(CustomerID);
    }
}
