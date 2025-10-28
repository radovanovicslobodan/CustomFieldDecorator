package com.example.utils;

import com.example.annotations.FindInside;
import java.lang.reflect.Field;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

public class ScopedElementLocator implements ElementLocator {

  private final WebElement container;
  private final By by;

  public ScopedElementLocator(WebElement container, Field field) {
    this.container = container;
    FindInside annotation = field.getAnnotation(FindInside.class);
    if (annotation == null) {
      throw new IllegalArgumentException(
          "Field " + field.getName() + " is not annotated with @FindInside");
    }
    this.by = FindInsideUtils.buildByFromAnnotation(annotation); // ✅ Call utility
  }

  @Override
  public WebElement findElement() {
    return container.findElement(by);
  }

  @Override
  public List<WebElement> findElements() {
    return container.findElements(by);
  }
}
