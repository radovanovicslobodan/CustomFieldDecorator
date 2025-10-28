package com.example.utils;

import java.lang.reflect.Field;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

public class ContainerFieldDecorator extends DefaultFieldDecorator {

  private final WebElement container;

  public ContainerFieldDecorator(WebElement container) {
    super(new ElementLocatorFactory() {
      @Override
      public ElementLocator createLocator(Field field) {
        // Inline the check here — no method call on 'this'
        Class<?> fieldType = field.getType();
        if (WebElement.class.isAssignableFrom(fieldType) ||
            List.class.isAssignableFrom(fieldType)) {
          return new ScopedElementLocator(container, field);
        }
        return null; // fallback to default behavior
      }
    });
    this.container = container;
  }
}
