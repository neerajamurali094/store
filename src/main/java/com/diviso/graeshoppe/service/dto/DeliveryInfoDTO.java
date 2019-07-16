package com.diviso.graeshoppe.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the DeliveryInfo entity.
 */
public class DeliveryInfoDTO implements Serializable {

    private Long id;

    private Instant startingTime;

    private Instant endTime;


    private Long storeId;

    private Long typeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Instant startingTime) {
        this.startingTime = startingTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeliveryInfoDTO deliveryInfoDTO = (DeliveryInfoDTO) o;
        if (deliveryInfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deliveryInfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeliveryInfoDTO{" +
            "id=" + getId() +
            ", startingTime='" + getStartingTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", store=" + getStoreId() +
            ", type=" + getTypeId() +
            "}";
    }
}
