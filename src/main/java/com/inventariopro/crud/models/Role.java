package com.inventariopro.crud.models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
 USER,
 ADMIN,
 EMPLOYEE;

 @JsonCreator
 public static Role fromString(String roleName) {
  for (Role role : Role.values()) {
   if (role.name().equalsIgnoreCase(roleName)) {
    return role;
   }
  }
  throw new IllegalArgumentException("Invalid role name: " + roleName);
 }
}