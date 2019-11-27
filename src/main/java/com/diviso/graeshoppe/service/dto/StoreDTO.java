package com.diviso.graeshoppe.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Store entity.
 */
public class StoreDTO implements Serializable {

    private Long id;

    private String regNo;

    private String name;

    @Lob
    private byte[] image;

    private String imageContentType;
    private Double totalRating;

    private String location;

    private String locationName;

    private Long contactNo;

    private Instant openingTime;

    private String email;

    private Instant closingTime;

    private String info;

    private Double minAmount;

    private Instant maxDeliveryTime;

    @NotNull
    private String storeUniqueId;

    @NotNull
    private String imageLink;


    private Long propreitorId;

    private Long storeAddressId;

    private Long storeSettingsId;

    private Long preOrderSettingsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Double totalRating) {
        this.totalRating = totalRating;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Long getContactNo() {
        return contactNo;
    }

    public void setContactNo(Long contactNo) {
        this.contactNo = contactNo;
    }

    public Instant getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Instant openingTime) {
        this.openingTime = openingTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Instant closingTime) {
        this.closingTime = closingTime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public Instant getMaxDeliveryTime() {
        return maxDeliveryTime;
    }

    public void setMaxDeliveryTime(Instant maxDeliveryTime) {
        this.maxDeliveryTime = maxDeliveryTime;
    }

    public String getStoreUniqueId() {
        return storeUniqueId;
    }

    public void setStoreUniqueId(String storeUniqueId) {
        this.storeUniqueId = storeUniqueId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Long getPropreitorId() {
        return propreitorId;
    }

    public void setPropreitorId(Long propreitorId) {
        this.propreitorId = propreitorId;
    }

    public Long getStoreAddressId() {
        return storeAddressId;
    }

    public void setStoreAddressId(Long storeAddressId) {
        this.storeAddressId = storeAddressId;
    }

    public Long getStoreSettingsId() {
        return storeSettingsId;
    }

    public void setStoreSettingsId(Long storeSettingsId) {
        this.storeSettingsId = storeSettingsId;
    }

    public Long getPreOrderSettingsId() {
        return preOrderSettingsId;
    }

    public void setPreOrderSettingsId(Long preOrderSettingsId) {
        this.preOrderSettingsId = preOrderSettingsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StoreDTO storeDTO = (StoreDTO) o;
        if (storeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), storeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StoreDTO{" +
            "id=" + getId() +
            ", regNo='" + getRegNo() + "'" +
            ", name='" + getName() + "'" +
            ", image='" + getImage() + "'" +
            ", totalRating=" + getTotalRating() +
            ", location='" + getLocation() + "'" +
            ", locationName='" + getLocationName() + "'" +
            ", contactNo=" + getContactNo() +
            ", openingTime='" + getOpeningTime() + "'" +
            ", email='" + getEmail() + "'" +
            ", closingTime='" + getClosingTime() + "'" +
            ", info='" + getInfo() + "'" +
            ", minAmount=" + getMinAmount() +
            ", maxDeliveryTime='" + getMaxDeliveryTime() + "'" +
            ", storeUniqueId='" + getStoreUniqueId() + "'" +
            ", imageLink='" + getImageLink() + "'" +
            ", propreitor=" + getPropreitorId() +
            ", storeAddress=" + getStoreAddressId() +
            ", storeSettings=" + getStoreSettingsId() +
            ", preOrderSettings=" + getPreOrderSettingsId() +
            "}";
    }
}
