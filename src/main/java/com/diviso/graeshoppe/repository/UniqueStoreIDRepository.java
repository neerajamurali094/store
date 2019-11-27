package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.UniqueStoreID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UniqueStoreID entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniqueStoreIDRepository extends JpaRepository<UniqueStoreID, Long> {

}
