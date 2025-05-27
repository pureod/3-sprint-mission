package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity {

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;     // BinaryContent

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus status;

//  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//  private List<ReadStatus> readStatuses = new ArrayList<>();


  public User(String username, String email, String password, BinaryContent profile,
      UserStatus status) {
    super();
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
    this.status = status;
  }

  public void update(String newUsername, String newEmail, String newPassword,
      BinaryContent newProfile, UserStatus newStatus) {
    if (newUsername != null && !newUsername.equals(this.username)) {
      this.username = newUsername;
    }
    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
    }
    if (newPassword != null && !newPassword.equals(this.password)) {
      this.password = newPassword;
    }
    if (newProfile != null && !newProfile.equals(this.profile)) {
      this.profile = newProfile;
    }
    if (newStatus != null && !newStatus.equals(this.status)) {
      this.status = newStatus;
    }
  }
}
