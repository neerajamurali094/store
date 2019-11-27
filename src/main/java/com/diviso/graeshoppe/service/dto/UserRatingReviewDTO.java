package com.diviso.graeshoppe.service.dto;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the UserRatingReview entity.
 */
public class UserRatingReviewDTO implements Serializable {

    private Long id;

    private String userName;

    private Double rating;

    private String review;

    private ZonedDateTime date;


    private Long storeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserRatingReviewDTO userRatingReviewDTO = (UserRatingReviewDTO) o;
        if (userRatingReviewDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userRatingReviewDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserRatingReviewDTO{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", rating=" + getRating() +
            ", review='" + getReview() + "'" +
            ", date='" + getDate() + "'" +
            ", store=" + getStoreId() +
            "}";
    }
}
