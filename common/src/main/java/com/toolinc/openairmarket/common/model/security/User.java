package com.toolinc.openairmarket.common.model.security;

import com.toolinc.openairmarket.common.model.Domain;

/** Marker interface to identify a user. */
public interface User extends Domain {

  String getEmail();
}
