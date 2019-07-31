package com.diviso.graeshoppe.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the StoreType entity.
 */
public class StoreTypeDTO implements Serializable {

    private Long id;

    private String name;


    private Long storeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

        StoreTypeDTO storeTypeDTO = (StoreTypeDTO) o;
        if (storeTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), storeTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StoreTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", store=" + getStoreId() +
            "}";
    }
}
