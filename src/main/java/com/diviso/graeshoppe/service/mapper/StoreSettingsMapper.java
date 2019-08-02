package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.StoreSettingsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StoreSettings and its DTO StoreSettingsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StoreSettingsMapper extends EntityMapper<StoreSettingsDTO, StoreSettings> {



    default StoreSettings fromId(Long id) {
        if (id == null) {
            return null;
        }
        StoreSettings storeSettings = new StoreSettings();
        storeSettings.setId(id);
        return storeSettings;
    }
}
