package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.PreOrderSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PreOrderSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PreOrderSettingsRepository extends JpaRepository<PreOrderSettings, Long> {

}
