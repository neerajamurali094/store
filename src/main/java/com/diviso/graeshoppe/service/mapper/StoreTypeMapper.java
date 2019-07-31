package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.StoreTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StoreType and its DTO StoreTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {StoreMapper.class})
public interface StoreTypeMapper extends EntityMapper<StoreTypeDTO, StoreType> {

    @Mapping(source = "store.id", target = "storeId")
    StoreTypeDTO toDto(StoreType storeType);

    @Mapping(source = "storeId", target = "store")
    StoreType toEntity(StoreTypeDTO storeTypeDTO);

    default StoreType fromId(Long id) {
        if (id == null) {
            return null;
        }
        StoreType storeType = new StoreType();
        storeType.setId(id);
        return storeType;
    }
}
