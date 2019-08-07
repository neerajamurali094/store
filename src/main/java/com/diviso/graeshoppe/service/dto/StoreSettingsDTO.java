package com.diviso.graeshoppe.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the StoreSettings entity.
 */
public class StoreSettingsDTO implements Serializable {

    private Long id;

    private Double deliveryCharge;

    private Double serviceCharge;

    private String orderAcceptType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(Double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public Double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getOrderAcceptType() {
        return orderAcceptType;
    }

    public void setOrderAcceptType(String orderAcceptType) {
        this.orderAcceptType = orderAcceptType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StoreSettingsDTO storeSettingsDTO = (StoreSettingsDTO) o;
        if (storeSettingsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), storeSettingsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StoreSettingsDTO{" +
            "id=" + getId() +
            ", deliveryCharge=" + getDeliveryCharge() +
            ", serviceCharge=" + getServiceCharge() +
            ", orderAcceptType='" + getOrderAcceptType() + "'" +
            "}";
    }
}
