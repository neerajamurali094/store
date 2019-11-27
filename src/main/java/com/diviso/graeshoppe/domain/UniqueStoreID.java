package com.diviso.graeshoppe.domain;



import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UniqueStoreID.
 */
@Entity
@Table(name = "unique_store_id")
@Document(indexName = "uniquestoreid")
public class UniqueStoreID implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        UniqueStoreID uniqueStoreID = (UniqueStoreID) o;
        if (uniqueStoreID.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), uniqueStoreID.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UniqueStoreID{" +
            "id=" + getId() +
            "}";
    }
}
