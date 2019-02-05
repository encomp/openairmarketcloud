package com.toolinc.openairmarket.common.persistence.model.security;

import com.toolinc.openairmarket.common.model.Domain;
import com.toolinc.openairmarket.common.model.security.User;
import com.toolinc.openairmarket.common.persistence.model.AbstractActiveModel;

/** Defines a system user. */
public final class SystemUser extends AbstractActiveModel implements User {

  private String email;

  public String getId() {
    return getEmail();
  }

  public void setId(String id) {
    setEmail(id);
  }

  @Override
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = checkNotEmpty(email);
  }

  /** Creates a new {@link SystemUser.Builder} instance. */
  public static final Builder newBuilder() {
    return new SystemUser.Builder().setActive(true);
  }

  /** Builder factory to creates instances of {@link SystemUser}. */
  public static final class Builder implements Domain {
    private String email;
    private boolean active;

    public Builder setEmail(String email) {
      this.email = checkNotEmpty(email);
      return this;
    }

    public Builder setActive(boolean active) {
      this.active = active;
      return this;
    }

    public SystemUser build() {
      SystemUser systemUser = new SystemUser();
      systemUser.setEmail(email);
      systemUser.setActive(active);
      return systemUser;
    }
  }
}
