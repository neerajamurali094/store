package com.diviso.graeshoppe.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Banner entity.
 */
public class BannerDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] file;

    private String fileContentType;
    private String imageLink;


    private Long storeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
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

        BannerDTO bannerDTO = (BannerDTO) o;
        if (bannerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bannerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BannerDTO{" +
            "id=" + getId() +
            ", file='" + getFile() + "'" +
            ", imageLink='" + getImageLink() + "'" +
            ", store=" + getStoreId() +
            "}";
    }
}
