package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.Banner;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Banner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

}
