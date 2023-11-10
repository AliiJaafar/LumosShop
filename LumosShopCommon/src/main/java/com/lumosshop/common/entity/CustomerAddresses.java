package com.lumosshop.common.entity;

import com.lumosshop.common.entity.control.Nation;
import jakarta.persistence.*;

@Entity
@Table(name = "customer_addresses")
public class CustomerAddresses {
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
    @ManyToOne
    @JoinColumn(name = "nation_Id")
    private Nation nation;
    @ManyToOne
    @JoinColumn(name = "customer_Id")
    private Customer customer;
    @Column(name = "is_primary")
    private boolean isPrimary;

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

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    @Override
    public String toString() {
        StringBuilder fullAddress = new StringBuilder();

        if (firstName != null) {
            fullAddress.append(firstName).append(" ");
        }
        if (lastName != null) {
            fullAddress.append(lastName).append(", ");
        }
        if (addressLine1 != null) {
            fullAddress.append(addressLine1).append(", ");
        }
        if (addressLine2 != null) {
            fullAddress.append(addressLine2).append(", ");
        }
        if (phoneNumber != null) {
            fullAddress.append(phoneNumber).append(", ");
        }
        if (nation.getName() != null) {
            fullAddress.append(nation.getName()).append(", ");
        }
        if (city != null) {
            fullAddress.append(city);
        }

        return fullAddress.toString();
    }

}
