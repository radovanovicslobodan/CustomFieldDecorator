package com.example.enums;

public enum Ingredient {

  // dummy comment
  CHEESE("Cheese"),
  MUSHROOM("Mushroom"),
  PEPPER("Pepper"),
  ONION("Onion");

  private final String displayName;

  Ingredient(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
