package com.example.components;

import com.example.annotations.FindInside;
import com.example.utils.ComponentFactory;
import org.openqa.selenium.WebElement;

public class HeaderComponent {

	// Locators inside header container using @FindInside
	@FindInside(className = "app_logo")
	private WebElement appLogo;

	@FindInside(id = "shopping_cart_container")
	private WebElement cartIcon;

	@FindInside(css = "select.product_sort_container")
	private WebElement sortDropdown;

	@FindInside(xpath = "//button[@id='react-burger-menu-btn']")
	private WebElement menuButton;

	public HeaderComponent(WebElement container) {

		ComponentFactory.initElements(container, this);
	}

	public String getLogoText() {

		return appLogo.getText();
	}

	public void openCart() {

		cartIcon.click();
	}

	public void openMenu() {

		menuButton.click();
	}

	public boolean isSortDropdownDisplayed() {

		return sortDropdown.isDisplayed();
	}
}
