package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.ReplyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Reply and its DTO ReplyDTO.
 */
@Mapper(componentModel = "spring", uses = {ReviewMapper.class, UserRatingReviewMapper.class})
public interface ReplyMapper extends EntityMapper<ReplyDTO, Reply> {

    @Mapping(source = "review.id", target = "reviewId")
    @Mapping(source = "userRatingReview.id", target = "userRatingReviewId")
    ReplyDTO toDto(Reply reply);

    @Mapping(source = "reviewId", target = "review")
    @Mapping(source = "userRatingReviewId", target = "userRatingReview")
    Reply toEntity(ReplyDTO replyDTO);

    default Reply fromId(Long id) {
        if (id == null) {
            return null;
        }
        Reply reply = new Reply();
        reply.setId(id);
        return reply;
    }
}
