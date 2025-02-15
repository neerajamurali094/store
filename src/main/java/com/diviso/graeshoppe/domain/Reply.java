package com.diviso.graeshoppe.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Reply.
 */
@Entity
@Table(name = "reply")
@Document(indexName = "reply")
public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "reply")
    private String reply;

    @Column(name = "replied_date")
    private ZonedDateTime repliedDate;

    @ManyToOne
    @JsonIgnoreProperties("replies")
    private Review review;

    @ManyToOne
    @JsonIgnoreProperties("replies")
    private UserRatingReview userRatingReview;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public Reply userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReply() {
        return reply;
    }

    public Reply reply(String reply) {
        this.reply = reply;
        return this;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public ZonedDateTime getRepliedDate() {
        return repliedDate;
    }

    public Reply repliedDate(ZonedDateTime repliedDate) {
        this.repliedDate = repliedDate;
        return this;
    }

    public void setRepliedDate(ZonedDateTime repliedDate) {
        this.repliedDate = repliedDate;
    }

    public Review getReview() {
        return review;
    }

    public Reply review(Review review) {
        this.review = review;
        return this;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public UserRatingReview getUserRatingReview() {
        return userRatingReview;
    }

    public Reply userRatingReview(UserRatingReview userRatingReview) {
        this.userRatingReview = userRatingReview;
        return this;
    }

    public void setUserRatingReview(UserRatingReview userRatingReview) {
        this.userRatingReview = userRatingReview;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reply reply = (Reply) o;
        if (reply.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reply.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reply{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", reply='" + getReply() + "'" +
            ", repliedDate='" + getRepliedDate() + "'" +
            "}";
    }
}
