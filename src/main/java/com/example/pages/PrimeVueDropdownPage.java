package com.example.pages;

import com.example.components.Dropdown;
import com.example.enums.City;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PrimeVueDropdownPage {

	private WebDriver driver;

	@FindBy(xpath = "//div[@id='pv_id_45']/parent::div") // container div with p-dropdown
	private WebElement cityDropdownContainer;

	public PrimeVueDropdownPage(WebDriver driver) {

		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void selectCity(City city) {

		Dropdown dropdown = new Dropdown(cityDropdownContainer, driver);
		dropdown.selectByVisibleText(city);
	}
}
