package com.example.utils;

import com.example.annotations.FindInside;
import org.openqa.selenium.By;

public class FindInsideUtils {

  public static By buildByFromAnnotation(FindInside annotation) {
    if (annotation == null) {
      throw new IllegalArgumentException("Annotation cannot be null");
    }

    if (!annotation.using().isEmpty()) {
      return By.cssSelector(annotation.using());
    }
    if (!annotation.id().isEmpty()) {
      return By.id(annotation.id());
    }
    if (!annotation.css().isEmpty()) {
      return By.cssSelector(annotation.css());
    }
    if (!annotation.xpath().isEmpty()) {
      return By.xpath(annotation.xpath());
    }
    if (!annotation.className().isEmpty()) {
      return By.className(annotation.className());
    }
    if (!annotation.name().isEmpty()) {
      return By.name(annotation.name());
    }
    if (!annotation.tagName().isEmpty()) {
      return By.tagName(annotation.tagName());
    }
    if (!annotation.linkText().isEmpty()) {
      return By.linkText(annotation.linkText());
    }
    if (!annotation.partialLinkText().isEmpty()) {
      return By.partialLinkText(annotation.partialLinkText());
    }

    throw new IllegalArgumentException(
        "No valid locator found in @FindInside. " +
            "Set one of: id, css, xpath, className, name, tagName, linkText, partialLinkText, or using."
    );
  }
}
