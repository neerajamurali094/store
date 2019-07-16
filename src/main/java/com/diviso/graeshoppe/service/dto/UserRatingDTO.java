package com.diviso.graeshoppe.service.dto;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the UserRating entity.
 */
public class UserRatingDTO implements Serializable {

    private Long id;

    private String userName;

    private Double rating;

    private ZonedDateTime ratedOn;


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

    public ZonedDateTime getRatedOn() {
        return ratedOn;
    }

    public void setRatedOn(ZonedDateTime ratedOn) {
        this.ratedOn = ratedOn;
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

        UserRatingDTO userRatingDTO = (UserRatingDTO) o;
        if (userRatingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userRatingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserRatingDTO{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", rating=" + getRating() +
            ", ratedOn='" + getRatedOn() + "'" +
            ", store=" + getStoreId() +
            "}";
    }
}
