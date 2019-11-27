package com.diviso.graeshoppe.domain;



import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A PreOrderSettings.
 */
@Entity
@Table(name = "pre_order_settings")
@Document(indexName = "preordersettings")
public class PreOrderSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_pre_order_available")
    private Boolean isPreOrderAvailable;

    @Column(name = "from_time")
    private ZonedDateTime fromTime;

    @Column(name = "to_time")
    private ZonedDateTime toTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsPreOrderAvailable() {
        return isPreOrderAvailable;
    }

    public PreOrderSettings isPreOrderAvailable(Boolean isPreOrderAvailable) {
        this.isPreOrderAvailable = isPreOrderAvailable;
        return this;
    }

    public void setIsPreOrderAvailable(Boolean isPreOrderAvailable) {
        this.isPreOrderAvailable = isPreOrderAvailable;
    }

    public ZonedDateTime getFromTime() {
        return fromTime;
    }

    public PreOrderSettings fromTime(ZonedDateTime fromTime) {
        this.fromTime = fromTime;
        return this;
    }

    public void setFromTime(ZonedDateTime fromTime) {
        this.fromTime = fromTime;
    }

    public ZonedDateTime getToTime() {
        return toTime;
    }

    public PreOrderSettings toTime(ZonedDateTime toTime) {
        this.toTime = toTime;
        return this;
    }

    public void setToTime(ZonedDateTime toTime) {
        this.toTime = toTime;
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
        PreOrderSettings preOrderSettings = (PreOrderSettings) o;
        if (preOrderSettings.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), preOrderSettings.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PreOrderSettings{" +
            "id=" + getId() +
            ", isPreOrderAvailable='" + isIsPreOrderAvailable() + "'" +
            ", fromTime='" + getFromTime() + "'" +
            ", toTime='" + getToTime() + "'" +
            "}";
    }
}
