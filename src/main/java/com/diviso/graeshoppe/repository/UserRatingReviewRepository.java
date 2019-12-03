package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.UserRatingReview;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserRatingReview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRatingReviewRepository extends JpaRepository<UserRatingReview, Long> {

	boolean existsByStoreIdAndUserName(Long storeId, String userName);

	UserRatingReview findByStoreIdAndUserName(Long storeId, String userName);
	
	@Query("SELECT COUNT(u) FROM UserRatingReviewRepository  u WHERE u.rating=:rating AND u.storeId = :storeId")
	public int getCount(@Param("rating")Double rating,@Param("storeId")Long storeId);

}
