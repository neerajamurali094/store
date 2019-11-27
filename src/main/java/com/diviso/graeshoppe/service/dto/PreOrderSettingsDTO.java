package com.diviso.graeshoppe.service.dto;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the PreOrderSettings entity.
 */
public class PreOrderSettingsDTO implements Serializable {

    private Long id;

    private Boolean isPreOrderAvailable;

    private ZonedDateTime fromTime;

    private ZonedDateTime toTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsPreOrderAvailable() {
        return isPreOrderAvailable;
    }

    public void setIsPreOrderAvailable(Boolean isPreOrderAvailable) {
        this.isPreOrderAvailable = isPreOrderAvailable;
    }

    public ZonedDateTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(ZonedDateTime fromTime) {
        this.fromTime = fromTime;
    }

    public ZonedDateTime getToTime() {
        return toTime;
    }

    public void setToTime(ZonedDateTime toTime) {
        this.toTime = toTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PreOrderSettingsDTO preOrderSettingsDTO = (PreOrderSettingsDTO) o;
        if (preOrderSettingsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), preOrderSettingsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PreOrderSettingsDTO{" +
            "id=" + getId() +
            ", isPreOrderAvailable='" + isIsPreOrderAvailable() + "'" +
            ", fromTime='" + getFromTime() + "'" +
            ", toTime='" + getToTime() + "'" +
            "}";
    }
}
