package dev.skullition.lockium.repository;

import dev.skullition.lockium.model.entity.AllowedSelfRole;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/** Repository for handling {@link AllowedSelfRole}. */
@Repository
public interface AllowedSelfRoleRepository extends CrudRepository<AllowedSelfRole, Long> {
  /**
   * Queries all roles in the database.
   *
   * @return list of roles
   */
  @Query("select role from AllowedSelfRole role")
  List<AllowedSelfRole> findAllList();
}
