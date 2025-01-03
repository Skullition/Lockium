package dev.skullition.lockium.repository;

import dev.skullition.lockium.model.entity.AllowedSelfRole;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/** Repository for handling {@link AllowedSelfRole}. */
@Repository
public interface AllowedSelfRoleRepository extends CrudRepository<AllowedSelfRole, Long> {

  /** Checks whether any of the ids are in the table. */
  boolean existsByIdIn(List<Long> ids);
}
