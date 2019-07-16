package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.BannerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Banner and its DTO BannerDTO.
 */
@Mapper(componentModel = "spring", uses = {StoreMapper.class})
public interface BannerMapper extends EntityMapper<BannerDTO, Banner> {

    @Mapping(source = "store.id", target = "storeId")
    BannerDTO toDto(Banner banner);

    @Mapping(source = "storeId", target = "store")
    Banner toEntity(BannerDTO bannerDTO);

    default Banner fromId(Long id) {
        if (id == null) {
            return null;
        }
        Banner banner = new Banner();
        banner.setId(id);
        return banner;
    }
}
