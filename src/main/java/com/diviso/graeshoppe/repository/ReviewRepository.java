package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.Review;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Review entity.
 */
@SuppressWarnings("unused")
@Repository
@Deprecated
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
