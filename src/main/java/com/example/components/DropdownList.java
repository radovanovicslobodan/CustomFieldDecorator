package com.example.components;

import com.example.annotations.FindInside;
import com.example.enums.City;
import com.example.utils.ComponentFactory;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DropdownList {

  protected WebDriver driver;
  protected final WebElement container;

  // Use your @FindInside to locate elements within the container
  @FindInside(tagName = "li")
  List<WebElement> options;

  public DropdownList(WebElement container, WebDriver driver) {

    this.driver = driver;
    this.container = container;
    ComponentFactory.initElements(container, this);
  }

  public void selectByVisibleText(City city) {

    // Wait for the dropdown list to appear in the DOM (it may be appended to body)
    WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
//    wait.until(ExpectedConditions.visibilityOf(container));

    // Find the option by visible text
    for (WebElement option : options) {
      String label = option.getText();
      if (city.getDisplayName().equals(label)) {
        if (!option.getAttribute("aria-disabled").equals("true")) {
          option.click();
          return;
        } else {
          throw new IllegalArgumentException("Option '" + city.getDisplayName() + "' is disabled.");
        }
      }
    }
    throw new NoSuchElementException(
        "Option '" + city.getDisplayName() + "' not found in dropdown.");
  }
}
