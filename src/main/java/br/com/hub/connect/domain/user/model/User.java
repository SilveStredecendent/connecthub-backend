package br.com.hub.connect.domain.user.model;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import br.com.hub.connect.domain.gamification.model.UserBadge;
import br.com.hub.connect.domain.shared.model.BaseEntity;
import br.com.hub.connect.domain.user.enums.UserRole;
import io.quarkus.panache.common.Page;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  public UserRole role = UserRole.STUDENT;

  @Column(nullable = false)
  public String name;

  @Column(unique = true, nullable = false)
  public String email;

  @Column(nullable = false)
  public String password;

  @Column(unique = true)
  public String CPF;

  public String phone;

  @Column(unique = true)
  public Long enrollmentId;

  public Double xp = 0.0;
  public Integer level = 1;

  @Column(name = "avatar_url")
  public String avatarUrl;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  public Set<UserBadge> badges;

  public static List<User> findAll(int page, int size) {
    return findAll()
        .page(Page.of(page, size))
        .list();
  }

  public static List<User> findAllActive(int page, int size) {
    return find("isActive = true")
        .page(Page.of(page, size))
        .list();
  }

  public static Optional<User> findActiveById(Long id) {
    return find("id = ?1 and isActive = true", id)
        .firstResultOptional();
  }

  public static long countAll() {
    return count();
  }

  public static long countActive() {
    return count("isActive = true");
  }

  public static List<User> findByRoleActive(UserRole role, int page, int size) {
    return find("role = ?1 and isActive = true", role)
        .page(Page.of(page, size))
        .list();
  }

  public static boolean existsByEmailActive(String email) {
    return count("email = ?1 and isActive = true", email) > 0;
  }

  public static boolean existsByEmailActiveExcludingId(String email, Long excludeId) {
    return count("email = ?1 and isActive = true and id != ?2", email, excludeId) > 0;
  }

  public static List<User> findByNameContaining(String name, int page, int size) {
    return find("LOWER(name) LIKE LOWER(?1) and isActive = true", "%" + name + "%")
        .page(Page.of(page, size))
        .list();
  }

  public static List<User> findTopByXp(int limit) {
    return find("isActive = true ORDER BY xp DESC")
        .page(Page.of(0, limit))
        .list();
  }

  public static List<User> findByEmailDomain(String domain, int page, int size) {
    return find("email LIKE ?1 and isActive = true", "%" + domain)
        .page(Page.of(page, size))
        .list();
  }

  @Override
  public String toString() {
    return String.format("User{id=%d, name='%s', email='%s', role=%s}",
        id, name, email, role);
  }
}
