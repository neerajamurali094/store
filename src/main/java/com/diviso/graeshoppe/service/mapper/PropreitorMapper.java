package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.PropreitorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Propreitor and its DTO PropreitorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PropreitorMapper extends EntityMapper<PropreitorDTO, Propreitor> {



    default Propreitor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Propreitor propreitor = new Propreitor();
        propreitor.setId(id);
        return propreitor;
    }
}
