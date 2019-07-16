package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.StoreAddress;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StoreAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoreAddressRepository extends JpaRepository<StoreAddress, Long> {

}
