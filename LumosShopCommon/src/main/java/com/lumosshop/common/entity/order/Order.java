package com.lumosshop.common.entity.order;

import com.lumosshop.common.entity.Customer;
import com.lumosshop.common.entity.CustomerAddresses;
import jakarta.persistence.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 60)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 60)
    private String lastName;
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "address_line1", nullable = false, length = 256)
    private String addressLine1;

    @Column(name = "address_line2", length = 256)
    private String addressLine2;
    @Column(name = "city")
    private String city;

    @Column(name = "nation")
    private String nation;

    @Enumerated(EnumType.STRING)
    private Order_Phase phase;

    @Enumerated(EnumType.STRING)
    private Payment_Choice paymentChoice;

    @ManyToOne
    @JoinColumn(name = "customer_Id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("Timestamp asc")
    private List<OrderFollowUp> orderFollowUps = new ArrayList<>();
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<Order_Summary> orderSummaries = new HashSet<>();
    private Date orderDate;
    private Date DeliverDate;

    private int NumberOfDaysToDeliver;
    private float VAT;
    private float totalPrice;

    private float shippingCharge;
    private float productCost;
    private float InterSum;

    public Order() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Order_Phase getPhase() {
        return phase;
    }

    public void setPhase(Order_Phase phase) {
        this.phase = phase;
    }

    public Payment_Choice getPaymentChoice() {
        return paymentChoice;
    }

    public void setPaymentChoice(Payment_Choice paymentChoice) {
        this.paymentChoice = paymentChoice;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<Order_Summary> getOrderSummaries() {
        return orderSummaries;
    }

    public void setOrderSummaries(Set<Order_Summary> orderSummaries) {
        this.orderSummaries = orderSummaries;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getDeliverDate() {
        return DeliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        DeliverDate = deliverDate;
    }

    public int getNumberOfDaysToDeliver() {
        return NumberOfDaysToDeliver;
    }

    public void setNumberOfDaysToDeliver(int numberOfDaysToDeliver) {
        NumberOfDaysToDeliver = numberOfDaysToDeliver;
    }

    public float getVAT() {
        return VAT;
    }

    public void setVAT(float VAT) {
        this.VAT = VAT;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(float shippingCharge) {
        this.shippingCharge = shippingCharge;
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

    public List<OrderFollowUp> getOrderFollowUps() {
        return orderFollowUps;
    }

    public void setOrderFollowUps(List<OrderFollowUp> purchaseFollowUps) {
        this.orderFollowUps = purchaseFollowUps;
    }

    @Transient
    public String getSummaryLocation() {
        return nation + ", " + city;
    }


    @Transient
    public String retrieveTheFullAddress() {
        String fullAddress = "";

        if (nation != null) {
            fullAddress += nation;
        }
        if (city != null) {
            fullAddress += ", " + city;
        }
        if (addressLine1 != null) {
            fullAddress += ", " + addressLine1;
        }
        if (addressLine2 != null) {
            fullAddress += ", " + addressLine2;
        }
        if (phoneNumber != null) {
            fullAddress += ", " + phoneNumber;
        }

        return fullAddress;
    }

    public void CustomerAddress() {
        setFirstName(customer.getFirstName());
        setLastName(customer.getLastName());
        setPhoneNumber(customer.getPhoneNumber());
        setAddressLine1(customer.getAddressLine1());
        setAddressLine2(customer.getAddressLine2());
        setCity(customer.getCity());
        setNation(customer.getNation().getName());
    }

    public void ShippingAddress(CustomerAddresses address) {
        setFirstName(address.getFirstName());
        setLastName(address.getLastName());
        setPhoneNumber(address.getPhoneNumber());
        setAddressLine1(address.getAddressLine1());
        setAddressLine2(address.getAddressLine2());
        setCity(address.getCity());
        setNation(address.getNation().getName());
    }

    @Transient
    public String getFormDeliveryDate() {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormatter.format(this.DeliverDate);
    }

    public void setFormDeliveryDate(String dateString) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.DeliverDate = dateFormatter.parse(dateString);
        } catch (ParseException ignored) {
        }
    }

    public String getFormattedOrderDate() {
        DateFormat dateFormatter = new SimpleDateFormat("dd MMM yy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.orderDate);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String month = dateFormatter.format(this.orderDate).split(" ")[1];
        String year = String.format("%02d", calendar.get(Calendar.YEAR) % 100);

        return String.format("%02d %s %s", day, month, year);
    }
    public boolean phaseExist(Order_Phase phase) {

        for (OrderFollowUp trace : orderFollowUps) {
            if (trace.getRemarks().equals(phase)) {
                return true;
            }
        }
        return false;
    }

    @Transient
    public boolean isREVOKED() {
        return phaseExist(Order_Phase.REVOKED);
    }

    @Transient
    public boolean isIN_PROGRESS() {
        return phaseExist(Order_Phase.IN_PROGRESS);
    }

    @Transient
    public boolean isPACKAGED() {
        return phaseExist(Order_Phase.PACKAGED);
    }

    @Transient
    public boolean isSECURED() {
        return phaseExist(Order_Phase.SECURED);
    }

    @Transient
    public boolean isRECEIVED() {
        return phaseExist(Order_Phase.RECEIVED);
    }

    @Transient
    public boolean isInTransit() {
        return phaseExist(Order_Phase.IN_TRANSIT);
    }

    @Transient
    public boolean isREVERTED() {
        return phaseExist(Order_Phase.REVERTED);
    }

    @Transient
    public boolean isPAID() {
        return phaseExist(Order_Phase.PAID);
    }

    @Transient
    public boolean isREFUNDED() {
        return phaseExist(Order_Phase.REFUNDED);
    }

    @Transient
    public boolean isCUSTOMER_REQUESTED_RETURN() {
        return phaseExist(Order_Phase.CUSTOMER_REQUESTED_RETURN);
    }

    @Transient
    public String getProductList() {
        StringBuilder productNamesBuilder = new StringBuilder();
        productNamesBuilder.append("<ul>");

        for (Order_Summary summary : orderSummaries) {
            productNamesBuilder.append("<li>").append(summary.getProduct().getName()).append("</li>");
        }

        productNamesBuilder.append("</ul>");

        return productNamesBuilder.toString();
    }
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", city='" + city + '\'' +
                ", nation='" + nation + '\'' +
                ", phase=" + phase +
                '}';
    }
}
