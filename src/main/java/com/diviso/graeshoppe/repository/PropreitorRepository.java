package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.Propreitor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Propreitor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PropreitorRepository extends JpaRepository<Propreitor, Long> {

}
