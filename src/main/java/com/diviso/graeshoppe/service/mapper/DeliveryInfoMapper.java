package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.DeliveryInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DeliveryInfo and its DTO DeliveryInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {StoreMapper.class, TypeMapper.class})
public interface DeliveryInfoMapper extends EntityMapper<DeliveryInfoDTO, DeliveryInfo> {

    @Mapping(source = "store.id", target = "storeId")
    @Mapping(source = "type.id", target = "typeId")
    DeliveryInfoDTO toDto(DeliveryInfo deliveryInfo);

    @Mapping(source = "storeId", target = "store")
    @Mapping(source = "typeId", target = "type")
    DeliveryInfo toEntity(DeliveryInfoDTO deliveryInfoDTO);

    default DeliveryInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setId(id);
        return deliveryInfo;
    }
}
