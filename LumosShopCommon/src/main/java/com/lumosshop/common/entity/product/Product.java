package com.lumosshop.common.entity.product;

import com.lumosshop.common.constant.Constants;
import com.lumosshop.common.entity.Category;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(unique = true, length = 256, nullable = false)
    private String name;

    @Column(unique = true, length = 256, nullable = false)
    private String alias;

    @Column(length = 512, nullable = false, name = "short_description")
    private String shortDescription;

    @Column(length = 4096, nullable = false, name = "full_description")
    private String fullDescription;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @Column(name = "created_time", nullable = false, updatable = false)
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    private boolean enabled;

    @Column(name = "in_stock")
    private boolean inStock;

    private float cost;

    private float price;

    @Column(name = "discount_percent")
    private float discountPercent;

    private float length;
    private float width;
    private float height;
    private float weight;

    @Column(name = "main_image", nullable = false)
    private String mainImage;

    private int reviews;
    private float rating_avg;


    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL ,orphanRemoval = true)
    private Set<ProductImage> images = new HashSet<>();
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    @OrderBy("name asc")
    private Set<ProductDetail> details = new HashSet<>();

    public Product() {
    }

    public Product(Integer id) {
        this.id = id;
    }

    public Product(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(float discountPercent) {
        this.discountPercent = discountPercent;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public Set<ProductImage> getImages() {
        return images;
    }

    public void setImages(Set<ProductImage> images) {
        this.images = images;
    }

    public void addExtraImage(String imageName) {
        this.images.add(new ProductImage(imageName, this));
    }


    @Transient
    public String getMainImagePath() {
        if (id == null || mainImage.isEmpty()) return "/images/lumosOnlyShape.png";

        return "/product-images/" + this.id + "/" + this.mainImage;
//        return Constants.B2_ADDRESS+ "/product-images/" + this.id + "/" + this.mainImage;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public float getRating_avg() {
        return rating_avg;
    }

    public void setRating_avg(float rating_avg) {
        this.rating_avg = rating_avg;
    }

    public Set<ProductDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<ProductDetail> details) {
        this.details = details;
    }

    public void addDetails(String name,String value) {
        this.details.add(new ProductDetail(name, value, this));
    }
    public void addDetails(Integer id,String name,String value) {
        this.details.add(new ProductDetail(id,name, value, this));
    }

    @Transient
    public String getAddressID() {
        return "/p/" + this.alias + "/";
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", discountPercent=" + discountPercent +
                '}';
    }

    public boolean containsImageName(String imageName) {
        Iterator<ProductImage> iterator =  images.iterator();
        while (iterator.hasNext()) {
            ProductImage image = iterator.next();

            if (image.getName().equals(imageName)) {
                return true;
            }

        }
        return false;
    }

    @Transient
    public float getDiscountPrice() {
        if (discountPercent > 0) {
            return price * ((100 - discountPercent) / 100);
        }
        return this.price;
    }

    @Transient
    private boolean reviewedAlready;
    @Transient
    private boolean customerIsAbleToWriteReview;

    public boolean isReviewedAlready() {
        return reviewedAlready;
    }

    public void setReviewedAlready(boolean reviewedAlready) {
        this.reviewedAlready = reviewedAlready;
    }

    public boolean isCustomerIsAbleToWriteReview() {
        return customerIsAbleToWriteReview;
    }

    public void setCustomerIsAbleToWriteReview(boolean customerIsAbleToWriteReview) {
        this.customerIsAbleToWriteReview = customerIsAbleToWriteReview;
    }
}
