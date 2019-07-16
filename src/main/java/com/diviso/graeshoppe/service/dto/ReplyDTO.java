package com.diviso.graeshoppe.service.dto;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Reply entity.
 */
public class ReplyDTO implements Serializable {

    private Long id;

    private String userName;

    private String reply;

    private ZonedDateTime repliedDate;


    private Long reviewId;

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

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public ZonedDateTime getRepliedDate() {
        return repliedDate;
    }

    public void setRepliedDate(ZonedDateTime repliedDate) {
        this.repliedDate = repliedDate;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReplyDTO replyDTO = (ReplyDTO) o;
        if (replyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), replyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReplyDTO{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", reply='" + getReply() + "'" +
            ", repliedDate='" + getRepliedDate() + "'" +
            ", review=" + getReviewId() +
            "}";
    }
}
