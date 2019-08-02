package com.diviso.graeshoppe.domain;



import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StoreSettings.
 */
@Entity
@Table(name = "store_settings")
@Document(indexName = "storesettings")
public class StoreSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delivery_charge")
    private Double deliveryCharge;

    @Column(name = "service_charge")
    private Double serviceCharge;

    @Column(name = "order_accept_type")
    private Double orderAcceptType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDeliveryCharge() {
        return deliveryCharge;
    }

    public StoreSettings deliveryCharge(Double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
        return this;
    }

    public void setDeliveryCharge(Double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public Double getServiceCharge() {
        return serviceCharge;
    }

    public StoreSettings serviceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
        return this;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Double getOrderAcceptType() {
        return orderAcceptType;
    }

    public StoreSettings orderAcceptType(Double orderAcceptType) {
        this.orderAcceptType = orderAcceptType;
        return this;
    }

    public void setOrderAcceptType(Double orderAcceptType) {
        this.orderAcceptType = orderAcceptType;
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
        StoreSettings storeSettings = (StoreSettings) o;
        if (storeSettings.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), storeSettings.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StoreSettings{" +
            "id=" + getId() +
            ", deliveryCharge=" + getDeliveryCharge() +
            ", serviceCharge=" + getServiceCharge() +
            ", orderAcceptType=" + getOrderAcceptType() +
            "}";
    }
}
