package com.dgpad.checkout;

import com.lumosshop.common.entity.Shipping;
import com.lumosshop.common.entity.ShoppingBag;
import com.lumosshop.common.entity.product.Product;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CheckoutServiceLayer {

    public CheckoutModel SettingUp(List<ShoppingBag> bagStocks, Shipping fee) {
        CheckoutModel checkoutModel = new CheckoutModel();

        float cost = determineTheCost(bagStocks);

        float totalPrice = determineTotalPrice(bagStocks);
        float shippingCharges = determineTotalShippingCharges(bagStocks, fee);
        float billTotal = totalPrice + shippingCharges;

        checkoutModel.setCost(cost);
        checkoutModel.setTotalPrice(totalPrice);
        checkoutModel.setShippingCharge(shippingCharges);
        checkoutModel.setBillTotal(billTotal);

        checkoutModel.setDeliveryETA(fee.getDayLong());
        checkoutModel.setCashOnDeliveryAbility(fee.isCashOnDelivery());

        return checkoutModel;
    }

    public float determineTotalShippingCharges(List<ShoppingBag> bagStocks, Shipping fee) {

        float TotalShippingCharge = 0.0f;

        for (ShoppingBag stock : bagStocks) {
            Product product = stock.getProduct();
            float dimensionalFactor = 139; // You might adjust this based on the carrier's specifications
            float dimensionalWeight = (product.getHeight() * product.getWidth()*product.getWeight()) / dimensionalFactor;
            float chargeableWeight = Math.max(product.getWeight(), dimensionalWeight);
            float shippingCharge = chargeableWeight * stock.getQuantity() * fee.getFeeRate();

            stock.setShippingCharge(shippingCharge);

            TotalShippingCharge += shippingCharge;
        }

        return TotalShippingCharge;
    }


    private float determineTotalPrice(List<ShoppingBag> bagStocks) {
        float totalPrice = 0.0f;

        for (ShoppingBag stock : bagStocks) {
            totalPrice += stock.getThePriceMultiplyByQty();
        }

        return totalPrice;
    }

    private float determineTheCost(List<ShoppingBag> bagStocks) {
        float totalCost = 0.0F;

        for (ShoppingBag item : bagStocks) {
            totalCost += item.getQuantity() * item.getProduct().getCost();
        }

        return totalCost;
    }
}
