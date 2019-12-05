package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.UserRating;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserRating entity.
 */
@SuppressWarnings("unused")
@Repository
@Deprecated
public interface UserRatingRepository extends JpaRepository<UserRating, Long> {

	@Query("SELECT COUNT(u) FROM UserRating u WHERE u.rating=:rating")
	public int getCount(@Param("rating")Double rating);
}
