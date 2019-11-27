package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.UniqueStoreIDDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UniqueStoreID and its DTO UniqueStoreIDDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UniqueStoreIDMapper extends EntityMapper<UniqueStoreIDDTO, UniqueStoreID> {



    default UniqueStoreID fromId(Long id) {
        if (id == null) {
            return null;
        }
        UniqueStoreID uniqueStoreID = new UniqueStoreID();
        uniqueStoreID.setId(id);
        return uniqueStoreID;
    }
}
