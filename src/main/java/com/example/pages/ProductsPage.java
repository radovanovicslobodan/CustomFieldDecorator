package com.example.pages;

import com.example.components.HeaderComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductsPage {

  private WebDriver driver;

  @FindBy(className = "title")
  private WebElement pageTitle;

  @FindBy(className = "header_container")
  private WebElement headerContainer;

  private HeaderComponent headerComponent;

  public ProductsPage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
    this.headerComponent = new HeaderComponent(headerContainer);
  }

  public String getPageTitle() {
    return pageTitle.getText();
  }

  public HeaderComponent getHeader() {
    return headerComponent;
  }
}
