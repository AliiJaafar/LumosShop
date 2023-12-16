package com.lumosshop.common.entity.review;

import com.lumosshop.common.entity.Customer;
import jakarta.persistence.*;

@Entity
@Table(name = "review_Likes")
public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int likes;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private static final int LIKE_POINT = 1;
    private static final int DISLIKE_POINT = -1;

    public void like() {
        this.likes = LIKE_POINT;
    }
    public void dislike() {
        this.likes = DISLIKE_POINT;
    }

    public ReviewLike() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    @Transient
    public boolean isLikedAlready() {
        return this.likes == LIKE_POINT;
    }
    @Transient
    public boolean isDislikedAlready() {
        return this.likes == DISLIKE_POINT;
    }

    @Override
    public String toString() {
        return "ReviewLike{" +
                "likes=" + likes +
                ", customer=" + customer +
                ", review=" + review +
                '}';
    }
}
