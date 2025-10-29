package com.example.enums;

public enum City {

	NEW_YORK("New York"),
	ROME("Rome"),
	LONDON("London"),
	ISTANBUL("Istanbul"),
	PARIS("Paris");

	private final String displayName;

	City(String displayName) {

		this.displayName = displayName;
	}

	public String getDisplayName() {

		return displayName;
	}
}
