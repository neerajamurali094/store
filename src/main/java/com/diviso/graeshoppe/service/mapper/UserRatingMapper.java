package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.UserRatingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserRating and its DTO UserRatingDTO.
 */
@Mapper(componentModel = "spring", uses = {StoreMapper.class})
public interface UserRatingMapper extends EntityMapper<UserRatingDTO, UserRating> {

    @Mapping(source = "store.id", target = "storeId")
    UserRatingDTO toDto(UserRating userRating);

    @Mapping(source = "storeId", target = "store")
    UserRating toEntity(UserRatingDTO userRatingDTO);

    default UserRating fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserRating userRating = new UserRating();
        userRating.setId(id);
        return userRating;
    }
}
