package com.diviso.graeshoppe.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the UniqueStoreID entity.
 */
public class UniqueStoreIDDTO implements Serializable {

    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UniqueStoreIDDTO uniqueStoreIDDTO = (UniqueStoreIDDTO) o;
        if (uniqueStoreIDDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), uniqueStoreIDDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UniqueStoreIDDTO{" +
            "id=" + getId() +
            "}";
    }
}
