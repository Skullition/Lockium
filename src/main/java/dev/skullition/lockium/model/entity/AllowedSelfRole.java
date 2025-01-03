package dev.skullition.lockium.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entity to store roles that are allowed to their own roles. */
@Entity
@Table(name = "allowed_self_role", schema = "lockium")
public class AllowedSelfRole {
  @Id
  @Column(name = "role_id", nullable = false)
  private Long id;

  @Column(name = "name")
  private String name;

  /** Entity to store roles that are allowed to their own roles. */
  public AllowedSelfRole() {}

  /** Entity to store roles that are allowed to their own roles. */
  public AllowedSelfRole(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
