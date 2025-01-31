package dev.skullition.lockium.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entity to store users' created roles. */
@Entity
@Table(name = "self_role", schema = "lockium")
public class SelfRole {
  @Id
  @Column(name = "role_id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
  private String name;

  /** Entity to store users' created roles. */
  public SelfRole() {}

  /** Entity to store users' created roles. */
  public SelfRole(String name, Long id) {
    this.name = name;
    this.id = id;
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
