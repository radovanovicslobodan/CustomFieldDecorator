package com.example.components;

import com.example.annotations.FindInside;
import com.example.enums.Ingredient;
import com.example.utils.ComponentFactory;
import org.openqa.selenium.WebElement;

import java.util.List;

public class RadioButtonList {

	@FindInside(css = "div.flex.items-center.gap-2")
	private List<RadioButton> radioButtons;

	public RadioButtonList(WebElement container) {

		// Initialize self using ComponentFactory-style decorator
		ComponentFactory.initElements(container, this);
	}

	/**
	 * Selects the radio button matching the enum's display name.
	 */
	public void selectOption(Ingredient option) {

		String targetName = option.getDisplayName();

		for (RadioButton rb : radioButtons) {
			if (rb.getLabel().equals(targetName)) {
				rb.select();
				return;
			}
		}

		throw new IllegalArgumentException("Radio option '" + targetName + "' not found.");
	}

	/**
	 * Returns the currently selected option, if any.
	 */
	public Ingredient getSelectedOption() {

		for (RadioButton rb : radioButtons) {
			if (rb.isSelected()) {
				try {
					return Ingredient.valueOf(rb.getLabel().toUpperCase().replace(" ", "_"));
				} catch (IllegalArgumentException e) {
					// Not in enum
				}
			}
		}
		return null;
	}

	/**
	 * Checks if an option exists and is enabled
	 */
	public boolean isOptionAvailable(Ingredient option) {

		return radioButtons.stream()
				.anyMatch(rb -> rb.getLabel().equals(option.getDisplayName()) && rb.isEnabled());
	}
}
