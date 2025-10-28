package com.example.pages;

import com.example.components.HeaderComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

  private WebDriver driver;

  // Standard login fields
  @FindBy(id = "user-name")
  private WebElement usernameInput;

  @FindBy(id = "password")
  private WebElement passwordInput;

  @FindBy(css = "input[type='submit']")
  private WebElement loginButton;

  // Container for header (used to initialize component)
  @FindBy(className = "header_container")
  private WebElement headerContainer;

  public LoginPage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  public ProductsPage loginAs(String username, String password) {
    usernameInput.sendKeys(username);
    passwordInput.sendKeys(password);
    loginButton.click();
    return new ProductsPage(driver);
  }

  public HeaderComponent getHeader() {
    return new HeaderComponent(headerContainer);
  }

  public boolean isHeaderVisible() {
    return headerContainer.isDisplayed();
  }
}
