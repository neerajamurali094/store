package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.StoreDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Store and its DTO StoreDTO.
 */
@Mapper(componentModel = "spring", uses = {PropreitorMapper.class, StoreAddressMapper.class, StoreSettingsMapper.class, PreOrderSettingsMapper.class})
public interface StoreMapper extends EntityMapper<StoreDTO, Store> {

    @Mapping(source = "propreitor.id", target = "propreitorId")
    @Mapping(source = "storeAddress.id", target = "storeAddressId")
    @Mapping(source = "storeSettings.id", target = "storeSettingsId")
    @Mapping(source = "preOrderSettings.id", target = "preOrderSettingsId")
    StoreDTO toDto(Store store);

    @Mapping(source = "propreitorId", target = "propreitor")
    @Mapping(source = "storeAddressId", target = "storeAddress")
    @Mapping(source = "storeSettingsId", target = "storeSettings")
    @Mapping(source = "preOrderSettingsId", target = "preOrderSettings")
    @Mapping(target = "storeTypes", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "userRatings", ignore = true)
    @Mapping(target = "banners", ignore = true)
    @Mapping(target = "deliveryInfos", ignore = true)
    @Mapping(target = "userRatingReviews", ignore = true)
    Store toEntity(StoreDTO storeDTO);

    default Store fromId(Long id) {
        if (id == null) {
            return null;
        }
        Store store = new Store();
        store.setId(id);
        return store;
    }
}
