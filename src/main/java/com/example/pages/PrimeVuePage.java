package com.example.pages;

import com.example.components.RadioButtonList;
import com.example.enums.Ingredient;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PrimeVuePage {

  private WebDriver driver;

  // Locate the group container
  @FindBy(css = "section.py-6:nth-child(3) .flex-wrap")
  private WebElement ingredientGroupContainer;

  private RadioButtonList ingredientList;

  public PrimeVuePage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);

    // Initialize component with container only
    this.ingredientList = new RadioButtonList(ingredientGroupContainer);
  }

  public void selectIngredient(Ingredient ingredient) {
    ingredientList.selectOption(ingredient);
  }

  public Ingredient getSelectedIngredient() {
    return ingredientList.getSelectedOption();
  }
}
