package com.dgpad.admin.Shipping;

import com.dgpad.admin.control.NationRepository;
import com.dgpad.admin.product.ProductRepository;
import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.Shipping;
import com.lumosshop.common.entity.control.Nation;
import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.exception.CustomerNotFoundException;
import com.lumosshop.common.exception.ProductNotFoundException;
import com.lumosshop.common.exception.ShippingFeeNotFoundException;
import com.stripe.model.ShippingRate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ShippingService {

    public static final int FEE_PER_PAGE = 8;

    @Autowired
    private ShippingRepository shippingRepository;
    @Autowired
    private NationRepository nationRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<Nation> nationList() {
        return nationRepository.findAllByOrderByNameAsc();
    }


    public List<Shipping> findAll() {
        return (List<Shipping>) shippingRepository.findAll();
    }

    public Page<Shipping> listFeesByPage(int pageNumber,
                                             String sortField,
                                             String sortDirection,
                                             String Keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, FEE_PER_PAGE, sort);

        if (Keyword != null) {
            return shippingRepository.findAll(Keyword, pageable);
        }
        return shippingRepository.findAll(pageable);
    }
    public void UpdateTheShippingCashOnDeliveryAbility(Integer id, boolean status) throws ShippingFeeNotFoundException {

        Long countById = shippingRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new ShippingFeeNotFoundException("could not found this Shipping Fee id - " + id);
        }
        shippingRepository.updateCashOnDeliveryAbility(id, status);
    }

    public Shipping save(Shipping shippingFee) {
        return shippingRepository.save(shippingFee);
    }

    public void deleteById(int id) throws ShippingFeeNotFoundException {

        Long countById = shippingRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new ShippingFeeNotFoundException("could not found this Shipping Fee id - " + id);
        }
        shippingRepository.deleteById(id);

    }

    public Shipping findShippingFeeById(int id) throws ShippingFeeNotFoundException {
        try {
            return shippingRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new ShippingFeeNotFoundException("Could not found this shipping fee id :  " + id);
        }

    }

    public Product findProductById(int id) throws ProductNotFoundException {
        try {
            return productRepository.findById(id).get();

        } catch (NoSuchElementException e) {
            throw new ProductNotFoundException("Could not found this Product id :  " + id);
        }

    }

    public float determineShippingCharge(Integer productId, Integer NationId, String city)
            throws ShippingFeeNotFoundException, ProductNotFoundException {
        Shipping fee = shippingRepository.findByNationAndCity(NationId, city);

        if (fee == null) {
            throw new ShippingFeeNotFoundException("Whoops! " +
                    "No luck finding a shipping rate for the destination. " +
                    "You're on manual mode now – gotta type in that shipping cost yourself!");
        }

        Product product = findProductById(productId);
        float dimensionalFactor = 139;
        float dimensionalWeight = (product.getHeight() * product.getWidth()*product.getWeight()) / dimensionalFactor;
        float chargeableWeight = Math.max(product.getWeight(), dimensionalWeight);
        return chargeableWeight * fee.getFeeRate();
    }



}
