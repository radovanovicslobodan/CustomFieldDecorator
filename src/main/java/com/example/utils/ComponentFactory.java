package com.example.utils;

import org.openqa.selenium.WebElement;

public class ComponentFactory {

  /**
   * Initializes elements of a component object by locating them within a given container element.
   *
   * @param container The parent WebElement that defines the scope (e.g., header, sidebar)
   * @param component The component instance whose fields should be initialized
   */
  public static void initElements(WebElement container, Object component) {
    if (container == null) {
      throw new IllegalArgumentException("Component container cannot be null. " +
          "Ensure @FindBy located the parent element before initializing.");
    }
    if (component == null) {
      throw new IllegalArgumentException("Component instance cannot be null");
    }

    try {
      org.openqa.selenium.support.PageFactory.initElements(
          new ContainerFieldDecorator(container),
          component
      );
    } catch (Exception e) {
      throw new IllegalStateException("Failed to initialize component elements " +
          "within container [" + container.getTagName() + "]", e);
    }
  }
}
