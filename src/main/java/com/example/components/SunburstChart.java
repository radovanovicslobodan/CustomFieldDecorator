package com.example.components;

import com.example.annotations.FindInside;
import com.example.utils.ComponentFactory;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SunburstChart {

  protected final WebElement container;
  protected final WebDriver driver;
  protected final WebDriverWait wait;

//  private final By sliceLocator = By.cssSelector("g.slice");

  @FindInside(css = ".slice")
  private List<WebElement> sliceElements;

  public SunburstChart(WebElement container, WebDriver driver) {

    this.container = container;
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    ComponentFactory.initElements(container, this);
  }

  public List<SunburstSlice> getSlices() {
    List<SunburstSlice> slices = new ArrayList<>();

    for (WebElement slice : sliceElements) {
      try {
        WebElement pathElement = slice.findElement(By.cssSelector("path.surface"));
        WebElement textElement = slice.findElement(By.cssSelector("text.slicetext"));
        slices.add(new SunburstSlice(pathElement, textElement));
      } catch (Exception e) {
        // Handle cases where text or path might be missing
        System.out.println("Skipping incomplete slice: " + e.getMessage());
      }
    }
    return slices;
  }
}
