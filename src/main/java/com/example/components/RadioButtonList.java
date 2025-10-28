package com.example.components;

import com.example.annotations.FindInside;
import com.example.enums.Ingredient;
import com.example.utils.ComponentFactory;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;

public class RadioButtonList {

  // Container: div with multiple radio+label groups
  private final WebElement container;

  // Individual items: each <div class="flex items-center gap-2">
  @FindInside(css = "div.flex.items-center.gap-2")
  private List<WebElement> itemContainers;

  private List<RadioButton> radioButtons;

  public RadioButtonList(WebElement container) {
    this.container = container;
    // Initialize self using ComponentFactory-style decorator
    ComponentFactory.initElements(container, this);

    // Create RadioButton components
    this.radioButtons = new ArrayList<>();
    for (WebElement item : itemContainers) {
      radioButtons.add(new RadioButton(item));
    }
  }

  /**
   * Selects the radio button matching the enum's display name.
   */
  public void selectOption(Ingredient option) {
    String targetName = option.getDisplayName();

    for (RadioButton rb : radioButtons) {
      if (rb.getLabel().equals(targetName)) {
        rb.select();
        return;
      }
    }

    throw new IllegalArgumentException("Radio option '" + targetName + "' not found.");
  }

  /**
   * Returns the currently selected option, if any.
   */
  public Ingredient getSelectedOption() {
    for (RadioButton rb : radioButtons) {
      if (rb.isSelected()) {
        try {
          return Ingredient.valueOf(rb.getLabel().toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
          // Not in enum
        }
      }
    }
    return null;
  }

  /**
   * Checks if an option exists and is enabled
   */
  public boolean isOptionAvailable(Ingredient option) {
    return radioButtons.stream()
        .anyMatch(rb -> rb.getLabel().equals(option.getDisplayName()) && rb.isEnabled());
  }
}
