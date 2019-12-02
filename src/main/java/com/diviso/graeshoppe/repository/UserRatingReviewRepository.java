package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.UserRatingReview;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserRatingReview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRatingReviewRepository extends JpaRepository<UserRatingReview, Long> {
	  boolean existsByStoreIdAndUserName(Long storeId,String userName);
	  UserRatingReview findByStoreIdAndUserName(Long storeId,String userName);
}
