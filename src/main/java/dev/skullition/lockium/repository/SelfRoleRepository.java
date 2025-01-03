package dev.skullition.lockium.repository;

import dev.skullition.lockium.model.entity.SelfRole;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/** Repository for handling {@link SelfRole SelfRoles}. */
@Repository
public interface SelfRoleRepository extends CrudRepository<SelfRole, Long> {
  /** Retrieves any of the matching roles in the table. */
  @Query("select role from SelfRole role where role.id in :roles")
  SelfRole findByIdIn(Collection<Long> roles);
}
