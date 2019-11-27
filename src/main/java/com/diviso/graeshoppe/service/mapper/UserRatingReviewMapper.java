package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.UserRatingReviewDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserRatingReview and its DTO UserRatingReviewDTO.
 */
@Mapper(componentModel = "spring", uses = {StoreMapper.class})
public interface UserRatingReviewMapper extends EntityMapper<UserRatingReviewDTO, UserRatingReview> {

    @Mapping(source = "store.id", target = "storeId")
    UserRatingReviewDTO toDto(UserRatingReview userRatingReview);

    @Mapping(source = "storeId", target = "store")
    UserRatingReview toEntity(UserRatingReviewDTO userRatingReviewDTO);

    default UserRatingReview fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserRatingReview userRatingReview = new UserRatingReview();
        userRatingReview.setId(id);
        return userRatingReview;
    }
}
