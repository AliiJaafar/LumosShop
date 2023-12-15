package com.lumosshop.common.entity.order;

import com.lumosshop.common.entity.Category;
import com.lumosshop.common.entity.product.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "order_summary")
public class Order_Summary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private float productCost;
    private float InterSum;
    private float shippingCharge;
    private float ItemPrice;
    private int Qty;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public Order_Summary() {
    }

    public Order_Summary(float productCost, float interSum, float shippingCharge, int qty, String category) {
        this.product = new Product();
        this.product.setCategory(new Category(category));

        this.productCost = productCost * qty;
        this.InterSum = interSum;
        this.shippingCharge = shippingCharge;
        this.Qty = qty;

    }
    public Order_Summary(String product,float productCost, float interSum, float shippingCharge, int qty) {
        this.product = new Product(product);

        this.productCost = productCost * qty;
        this.InterSum = interSum;
        this.shippingCharge = shippingCharge;
        this.Qty = qty;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getProductCost() {
        return productCost;
    }

    public void setProductCost(float productPrice) {
        this.productCost = productPrice;
    }

    public float getInterSum() {
        return InterSum;
    }

    public void setInterSum(float interSum) {
        InterSum = interSum;
    }

    public float getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(float shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public float getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(float itemPrice) {
        ItemPrice = itemPrice;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
