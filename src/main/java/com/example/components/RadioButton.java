package com.example.components;

import com.example.annotations.FindInside;
import com.example.utils.ComponentFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RadioButton {

	// Root container: <div class="flex items-center gap-2">
	private final WebElement container;

	// The actual clickable PrimeVue component
	@FindInside(css = "div.p-radiobutton")
	private WebElement radioButton;

	// Label next to it
	@FindInside(xpath = ".//label[@for]")
	private WebElement label;

	public RadioButton(WebElement container) {

		this.container = container;
		ComponentFactory.initElements(container, this);
	}

	public void select() {

		if (!isSelected()) {
			radioButton.click();
		}
	}

	public boolean isSelected() {

		return "true".equals(radioButton.getAttribute("data-p-checked"));
	}

	public boolean isEnabled() {

		return !"true".equals(radioButton.getAttribute("data-p-disabled"));
	}

	public String getLabel() {

		return label.getText().trim();
	}

	public String getValue() {

		return radioButton.getAttribute("data-p-value"); // or extract from input if needed
	}

	// Helper: Get associated input's value
	public String getInputValue() {

		WebElement input = container.findElement(By.cssSelector("input[type='radio']"));
		return input.getAttribute("value");
	}
}
