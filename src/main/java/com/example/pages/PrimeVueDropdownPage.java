package com.example.pages;

import com.example.components.Dropdown;
import com.example.enums.City;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PrimeVueDropdownPage {

	private WebDriver driver;
	private Dropdown dropdown;

	@FindBy(xpath = "//div[@id='pv_id_45']/parent::div") // container div with p-dropdown
	private WebElement cityDropdownContainer;

	public PrimeVueDropdownPage(WebDriver driver) {

		this.driver = driver;
		PageFactory.initElements(driver, this);
		dropdown = new Dropdown(cityDropdownContainer, driver);
	}

	public void selectCity(City city) {

		dropdown.selectByVisibleText(city);
	}

	public String getSelectedCity() {

		return dropdown.getSelectedText();
	}
}
