package com.example.components;

import com.example.annotations.FindInside;
import com.example.enums.City;
import com.example.utils.ComponentFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Dropdown {

	private WebDriver driver;
	private final WebElement container;

	// Use your @FindInside to locate elements within the container
	@FindInside(id = "pv_id_45")
	private WebElement trigger;

	@FindInside(css = "[aria-controls='pv_id_45_list']")
	private WebElement triggerIndicator;

	public Dropdown(WebElement container, WebDriver driver) {

		this.driver = driver;
		this.container = container;
		ComponentFactory.initElements(container, this);
	}

	public void selectByVisibleText(City city) {

		openDropdown();

		// Build the ID of the list from the trigger's ID
		String triggerId = triggerIndicator.getAttribute("aria-controls");
		if (triggerId == null || triggerId.isEmpty()) {
			throw new IllegalStateException(
					"Dropdown trigger must have an 'aria-controls' attribute for PrimeVue compatibility.");
		}

		// Wait for the dropdown list to appear in the DOM (it may be appended to body)
		WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
		WebElement dropdownList = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(triggerId)));

		// Find the option by visible text
		List<WebElement> options = dropdownList.findElements(By.cssSelector("li.p-dropdown-item"));
		for (WebElement option : options) {
			String label = option.findElement(By.className("p-dropdown-item-label")).getText();
			if (city.getDisplayName().equals(label)) {
				if (!option.getAttribute("aria-disabled").equals("true")) {
					option.click();
					return;
				} else {
					throw new IllegalArgumentException("Option '" + city.getDisplayName() + "' is disabled.");
				}
			}
		}
		throw new NoSuchElementException("Option '" + city.getDisplayName() + "' not found in dropdown.");
	}

	public String getSelectedText() {

		try {
			WebElement label = trigger.findElement(By.className("p-dropdown-label"));
			String text = label.getText().trim();
			// Handle placeholder vs selected value
			// PrimeVue shows placeholder if nothing selected (e.g., "Select a City")
			// You may want to return null or empty if placeholder is shown
			return text.isEmpty() || isPlaceholder(label) ? null : text;
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	private boolean isPlaceholder(WebElement label) {
		// PrimeVue adds .p-placeholder class when showing placeholder
		return label.getAttribute("class") != null &&
				label.getAttribute("class").contains("p-placeholder");
	}

	private void openDropdown() {

		if (!"true".equals(trigger.getAttribute("aria-expanded"))) {
			trigger.click();
			// Wait until expanded
			new WebDriverWait(this.driver, Duration.ofSeconds(5))
					.until(ExpectedConditions.attributeToBe(triggerIndicator, "aria-expanded", "true"));
		}
	}
}
