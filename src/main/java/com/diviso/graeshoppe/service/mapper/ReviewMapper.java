package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.ReviewDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Review and its DTO ReviewDTO.
 */
@Mapper(componentModel = "spring", uses = {StoreMapper.class})
public interface ReviewMapper extends EntityMapper<ReviewDTO, Review> {

    @Mapping(source = "store.id", target = "storeId")
    ReviewDTO toDto(Review review);

    @Mapping(source = "storeId", target = "store")
    @Mapping(target = "replies", ignore = true)
    Review toEntity(ReviewDTO reviewDTO);

    default Review fromId(Long id) {
        if (id == null) {
            return null;
        }
        Review review = new Review();
        review.setId(id);
        return review;
    }
}
