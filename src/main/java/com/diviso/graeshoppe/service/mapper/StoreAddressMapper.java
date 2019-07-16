package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.StoreAddressDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StoreAddress and its DTO StoreAddressDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StoreAddressMapper extends EntityMapper<StoreAddressDTO, StoreAddress> {



    default StoreAddress fromId(Long id) {
        if (id == null) {
            return null;
        }
        StoreAddress storeAddress = new StoreAddress();
        storeAddress.setId(id);
        return storeAddress;
    }
}
